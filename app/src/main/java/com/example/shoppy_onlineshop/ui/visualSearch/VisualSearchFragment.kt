package com.example.shoppy_onlineshop.ui.visualSearch

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import com.example.shoppy_onlineshop.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.shoppy_onlineshop.databinding.FragmentVisualSearchBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class VisualSearchFragment : Fragment() {
    private var _binding: FragmentVisualSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var imageCapture: ImageCapture

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d("CAMERA_DEBUG", "Permission granted via launcher")
            startCamera()
        } else {
            Log.e("CAMERA_DEBUG", "Permission denied via launcher")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVisualSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("CAMERA_DEBUG", "Permission already granted")
            startCamera()
        } else {
            Log.d("CAMERA_DEBUG", "Launching permission request")
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        binding.scanButton.setOnClickListener {
            Log.d("CAMERA_DEBUG", "Scan button clicked")
            takePictureAndAnalyze()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        imageCapture = ImageCapture.Builder().build()

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.surfaceProvider = binding.cameraPreview.surfaceProvider
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                Log.e("CAMERA_DEBUG", "Camera binding failed", e)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePictureAndAnalyze() {
        imageCapture.takePicture(
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(imageProxy: ImageProxy) {
                    val bitmap = imageProxy.toBitmap()
                    imageProxy.close()
                    analyzeImage(bitmap)
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("CAMERA_DEBUG", "Capture failed: ${exception.message}")
                }
            }
        )
    }

    private fun analyzeImage(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)

        val options = ImageLabelerOptions.Builder()
            .setConfidenceThreshold(0.85f) // Try 0.7, 0.8, or even 0.85 if needed
            .build()

        val labeler = ImageLabeling.getClient(options)

        labeler.process(image)
            .addOnSuccessListener { labels ->
                val detectedLabels = labels.map { it.text }
                Log.d("MLKit", "Detected labels: $detectedLabels")

                // ✅ Bundle and navigate
                val bundle = Bundle().apply {
                    putStringArray("labels", detectedLabels.toTypedArray())
                }

                findNavController().navigate(
                    R.id.action_visualSearchFragment_to_visualSearchResultFragment,
                    bundle
                )
            }
            .addOnFailureListener { e ->
                Log.e("MLKit", "Labeling failed", e)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        cameraExecutor.shutdown()
    }
}

// Extension function to convert ImageProxy to Bitmap
fun ImageProxy.toBitmap(): Bitmap? {
    val buffer = planes[0].buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}
