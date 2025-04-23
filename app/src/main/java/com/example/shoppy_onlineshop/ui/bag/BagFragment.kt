package com.example.shoppy_onlineshop.ui.bag

import com.example.shoppy_onlineshop.ui.userProfile.Orders.OrdersViewModel
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppy_onlineshop.R
import com.example.shoppy_onlineshop.databinding.FragmentBagBinding
import com.example.shoppy_onlineshop.helpers.setupSwipeToDelete
import com.example.shoppy_onlineshop.ui.userProfile.Orders.Order
import com.example.shoppy_onlineshop.ui.userProfile.addresses.EditAddressBottomSheet
import com.example.shoppy_onlineshop.ui.userProfile.paymentMethods.EditPaymentMethodBottomSheet
import com.example.shoppy_onlineshop.ui.userProfile.paymentMethods.PaymentMethodsViewModel
import com.google.firebase.auth.FirebaseAuth

class BagFragment : Fragment() {

    private val bagViewModel: BagViewModel by activityViewModels()
    private val ordersViewModel: OrdersViewModel by activityViewModels()

    private lateinit var bagAdapter: BagAdapter
    private lateinit var currentUserID: String

    private var _binding: FragmentBagBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBagBinding.inflate(inflater, container, false)
        currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        bagViewModel.loadBagProducts(currentUserID)

        setupRecyclerView()
        observeBagItems()
        setupSwipeToDelete()

        binding.checkoutButton.setOnClickListener {
                showCheckoutConfirmationDialog()
        }

        return binding.root
    }



    private fun setupRecyclerView() {
        binding.bagRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        bagAdapter = BagAdapter(emptyList(),
            onIncreaseQuantity = { productId -> bagViewModel.increaseQuantity(currentUserID, productId) },
            onDecreaseQuantity = { productId -> bagViewModel.decreaseQuantity(currentUserID, productId) },
            onRequestDeleteConfirmation = { productId -> showDeleteConfirmationDialog(productId) }
        )
        binding.bagRecyclerView.adapter = bagAdapter
    }

    private fun observeBagItems() {
        bagViewModel.bagItems.observe(viewLifecycleOwner) { cartItems ->
            bagAdapter.updateItems(cartItems)
            updateUI(cartItems)
        }
    }

    private fun setupSwipeToDelete() {
        setupSwipeToDelete(binding.bagRecyclerView) { position ->
            if (position in 0 until bagAdapter.itemCount) {
                val deletedItem = bagAdapter.getItem(position)
                bagViewModel.removeProductFromBag(currentUserID, deletedItem.product.id)
            }
        }
    }

    private fun updateUI(items: List<BagItem>) {
        val isEmpty = items.isEmpty()
        binding.bagRecyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
        binding.priceSummaryLayout.visibility = if (isEmpty) View.GONE else View.VISIBLE
        binding.emptyBagLayout.visibility = if (isEmpty) View.VISIBLE else View.GONE

        if (!isEmpty) {
            val subtotal = items.sumOf { it.product.price * it.quantity }
            val shipping = if (subtotal >= 100.0) 0.0 else 9.99
            val total = subtotal + shipping

            binding.priceSubtotal.text = "Subtotal: $%.2f".format(subtotal)
            binding.priceShipping.text = "Shipping: $%.2f".format(shipping)
            binding.priceTotal.text = "Total: $%.2f".format(total)
        }
    }

    private fun generateOrderId(): String = "ORD-${System.currentTimeMillis()}"

    private fun showDeleteConfirmationDialog(productId: Int) {
        val dialogView = layoutInflater.inflate(R.layout.custom_remove_dialog, null)
        val alertDialog = AlertDialog.Builder(requireContext()).setView(dialogView).create()

        dialogView.findViewById<Button>(R.id.No_button).setOnClickListener { alertDialog.dismiss() }
        dialogView.findViewById<Button>(R.id.Yes_button).setOnClickListener {
            bagViewModel.removeProductFromBag(currentUserID, productId)
            alertDialog.dismiss()
        }

        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog.show()
    }

    private fun showCheckoutConfirmationDialog() {
        val dialogView = layoutInflater.inflate(R.layout.custom_remove_dialog, null)
        val alertDialog = AlertDialog.Builder(requireContext()).setView(dialogView).create()

        dialogView.findViewById<TextView>(R.id.dialogTitle).text = "Confirm Purchase"
        dialogView.findViewById<TextView>(R.id.dialogMessage).text = "Are you sure you want to proceed with your order?"

        dialogView.findViewById<Button>(R.id.No_button).setOnClickListener { alertDialog.dismiss() }

        dialogView.findViewById<Button>(R.id.Yes_button).setOnClickListener {
            val sharedPrefs = requireContext().getSharedPreferences("addresses_prefs", Context.MODE_PRIVATE)
            val addressList = sharedPrefs.getString("addresses_list", "[]")
            val hasAddresses = !addressList.isNullOrBlank() && addressList != "[]"

            val defaultPaymentExists = PaymentMethodsViewModel(requireActivity().application)
                .paymentMethods.value?.any { it.isDefault } == true


            if (!hasAddresses || !defaultPaymentExists) {
                if (!hasAddresses) {
                    Toast.makeText(requireContext(), "Please add an address before placing an order", Toast.LENGTH_SHORT).show()

                    val sheet = EditAddressBottomSheet()
                    sheet.listener = object : EditAddressBottomSheet.OnAddressSavedListener {
                        override fun onAddressSaved() {
                            alertDialog.dismiss()
                        }
                    }
                    sheet.show(parentFragmentManager, "EditAddressBottomSheet")
                }

                if (!defaultPaymentExists) {
                    Toast.makeText(requireContext(), "Please add a payment method before placing an order", Toast.LENGTH_SHORT).show()

                    val paymentSheet = EditPaymentMethodBottomSheet()
                    paymentSheet.show(parentFragmentManager, "EditPaymentMethodBottomSheet")
                }

                return@setOnClickListener

            }

            val orderId = generateOrderId()
            val bagItems = bagViewModel.bagItems.value.orEmpty()

            val order = Order(
                orderId = orderId,
                userId = currentUserID,
                items = bagItems,
                status = "In Process",
                timestamp = System.currentTimeMillis()
            )

            val bundle = Bundle().apply {
                putParcelable("order", order)
            }

            findNavController().navigate(R.id.checkoutSummaryFragment, bundle)
            alertDialog.dismiss()

        }

        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
