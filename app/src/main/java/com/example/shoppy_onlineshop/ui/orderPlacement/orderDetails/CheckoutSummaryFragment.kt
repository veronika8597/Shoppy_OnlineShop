package com.example.shoppy_onlineshop.ui.orderPlacement.orderDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppy_onlineshop.R
import com.example.shoppy_onlineshop.databinding.FragmentCheckoutSummaryBinding
import com.example.shoppy_onlineshop.ui.bag.BagViewModel
import com.example.shoppy_onlineshop.ui.userProfile.Orders.Order
import com.example.shoppy_onlineshop.ui.userProfile.Orders.OrdersViewModel
import com.example.shoppy_onlineshop.ui.userProfile.addresses.AddressesViewModel
import com.example.shoppy_onlineshop.ui.userProfile.addresses.EditAddressBottomSheet
import com.example.shoppy_onlineshop.ui.userProfile.paymentMethods.EditPaymentMethodBottomSheet
import com.example.shoppy_onlineshop.ui.userProfile.paymentMethods.PaymentMethodsViewModel
import com.google.firebase.auth.FirebaseAuth


class CheckoutSummaryFragment : Fragment() {
    private lateinit var binding: FragmentCheckoutSummaryBinding
    private val addressViewModel: AddressesViewModel by activityViewModels()
    private val paymentViewModel: PaymentMethodsViewModel by activityViewModels()
    private val bagViewModel: BagViewModel by activityViewModels()
    private val ordersViewModel: OrdersViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckoutSummaryBinding.inflate(inflater, container, false)

        setupAddressSection()
        setupPaymentSection()
        setupProductSummary()

        binding.placeOrderButton.setOnClickListener {
            val selectedAddressId = binding.addressRadioGroup.checkedRadioButtonId
            val selectedPaymentId = binding.paymentMethodRadioGroup.checkedRadioButtonId

            if (selectedAddressId == -1 || selectedPaymentId == -1) {
                Toast.makeText(requireContext(), "Please select both address and payment method", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val orderId = "ORD-${System.currentTimeMillis()}"
            val bagItems = bagViewModel.bagItems.value.orEmpty()

            val order = Order(
                orderId = orderId,
                userId = FirebaseAuth.getInstance().currentUser!!.uid,
                items = bagItems,
                status = "In Process",
                timestamp = System.currentTimeMillis()
            )

            ordersViewModel.saveOrderToFirebase(order.userId, order,
                onSuccess = {
                    val bundle = Bundle().apply { putParcelable("order", order) }
                    findNavController().navigate(R.id.action_checkoutSummaryFragment_to_orderConfirmationFragment, bundle)
                },
                onFailure = { error: Exception ->
                    Toast.makeText(requireContext(), "Failed to place order: ${error.message}", Toast.LENGTH_LONG).show()
                }
            )
        }

        return binding.root
    }

    private fun setupAddressSection() {
        val addressGroup = binding.addressRadioGroup
        val addresses = addressViewModel.addresses.value.orEmpty()

        addresses.forEachIndexed { index, address ->
            val rb = RadioButton(requireContext()).apply {
                text = "${address.streetAddress}, ${address.city}"
                id = index
                isChecked = address.isDefault
                typeface = ResourcesCompat.getFont(requireContext(), R.font.fredoka)
                textSize = 16f
            }
            addressGroup.addView(rb)
        }

        val addNew = RadioButton(requireContext()).apply {
            text = "Add new address"
            id = addresses.size
            typeface = ResourcesCompat.getFont(requireContext(), R.font.fredoka)
            textSize = 16f
        }
        addressGroup.addView(addNew)

        addressGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == addresses.size) {
                EditAddressBottomSheet().show(parentFragmentManager, "EditAddressBottomSheet")
            }
        }
    }

    private fun setupPaymentSection() {
        val paymentGroup = binding.paymentMethodRadioGroup
        val methods = paymentViewModel.paymentMethods.value.orEmpty()

        methods.forEachIndexed { index, method ->
            val rb = RadioButton(requireContext()).apply {
                text = "**** **** **** ${method.cardNumber.takeLast(4)} - ${method.cardholderName}"
                id = index
                isChecked = method.isDefault
                typeface = ResourcesCompat.getFont(requireContext(), R.font.fredoka)
            }
            paymentGroup.addView(rb)
        }

        val addNew = RadioButton(requireContext()).apply {
            text = "Add new payment method"
            id = methods.size
            typeface = ResourcesCompat.getFont(requireContext(), R.font.fredoka)
        }
        paymentGroup.addView(addNew)

        paymentGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == methods.size) {
                EditPaymentMethodBottomSheet().show(parentFragmentManager, "EditPaymentMethodBottomSheet")
            }
        }
    }

    private fun setupProductSummary() {
        val items = bagViewModel.bagItems.value.orEmpty()
        binding.checkoutItemsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.checkoutItemsRecyclerView.adapter = CheckoutSummaryAdapter(items)

        val total = items.sumOf { it.product.price * it.quantity }
        binding.totalAmountText.text = "₪%.2f".format(total)
    }
}