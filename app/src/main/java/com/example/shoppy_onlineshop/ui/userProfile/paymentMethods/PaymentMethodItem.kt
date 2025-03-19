package com.example.shoppy_onlineshop.ui.userProfile.paymentMethods

class PaymentMethodItem (val id: Int, val cardNumber: String, val expiryDate: String, val cvv: String, val cardholderName: String, var isDefault: Boolean = false) {
}