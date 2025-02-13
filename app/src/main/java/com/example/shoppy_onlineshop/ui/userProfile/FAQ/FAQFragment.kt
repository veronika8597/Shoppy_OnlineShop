package com.example.shoppy_onlineshop.ui.userProfile.FAQ

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppy_onlineshop.R

class FAQFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var faqAdapter: FAQAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_faq, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewFAQ)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val faqList = listOf(
            FAQItem("1. How do I create an account?", "You can create an account by tapping on the Sign Up button on the login screen."),
            FAQItem("2. Do I need an account to shop?", "Yes, an account is required to place orders, track shipments, and save your favorite items."),
            FAQItem("3. How do I reset my password?", "Tap on Forgot Password? on the login screen. You will receive an email with a reset link."),
            FAQItem("4. How can I track my order?", "Go to **My Orders** in the **My Account** section. Select the order you want to track, and you’ll see real-time updates on its status."),
            FAQItem("5. What payment methods do you accept?", "We accept:  \n" +
                    "- Credit/Debit Cards (Visa, MasterCard, etc.)  \n" +
                    "- PayPal  \n" +
                    "- Apple Pay / Google Pay  "),
            FAQItem("6. Can I cancel or change my order after placing it?", "Orders can be canceled or modified within **1 hour** of placing them. Go to **My Orders** and select the order you wish to change."),
            FAQItem("7. How do I return an item?", "You can request a return from **My Orders** within **30 days** of delivery. Follow the return instructions provided for eligible items."),
            FAQItem("8. How long does shipping take?", "Shipping times depend on your location and the chosen shipping method:  \n" +
                    "- **Standard Shipping:** 5-7 business days  \n" +
                    "- **Express Shipping:** 2-3 business days"),
            FAQItem("9. What if my order is delayed?", "Sometimes delays happen due to high demand or carrier issues. You can check your order status under **My Orders** or contact our support team for assistance."),
            FAQItem("10. How can I contact customer support?", "You can reach us through:  \n" +
                    "\uD83D\uDCE7 **Email:** support@shoppy.com  \n" +
                    "\uD83D\uDCDE **Phone:** +1 (123) 456-7890")
        )

        faqAdapter = FAQAdapter(faqList)
        recyclerView.adapter = faqAdapter
    }
}
