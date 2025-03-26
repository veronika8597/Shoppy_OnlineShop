package com.example.shoppy_onlineshop.api

data class StoreProduct(
    val id: Int = 0, // Default value: 0
    val title: String = "", // Default value: empty string
    val description: String = "", // Default value: empty string
    val category: String = "", // Default value: empty string
    val price: Double = 0.0, // Default value: 0.0
    val discountPercentage: Double = 0.0, // Default value: 0.0
    val rating: Double = 0.0, // Default value: 0.0
    val stock: Int = 0, // Default value: 0
    val tags: List<String> = emptyList(), // Default value: empty list
    val brand: String = "", // Default value: empty string
    val sku: String = "", // Default value: empty string
    val weight: Int = 0, // Default value: 0
    val dimensions: Dimensions = Dimensions(), // Default value: default Dimensions
    val warrantyInformation: String = "", // Default value: empty string
    val shippingInformation: String = "", // Default value: empty string
    val availabilityStatus: String = "", // Default value: empty string
    val reviews: List<Review> = emptyList(), // Default value: empty list
    val returnPolicy: String = "", // Default value: empty string
    val minimumOrderQuantity: Int = 0, // Default value: 0
    val meta: Meta = Meta(), // Default value: default Meta
    val images: List<String> = emptyList(), // Default value: empty list
    val thumbnail: String = "" // Default value: empty string
)

data class Dimensions(
    val width: Double = 0.0, // Default value: 0.0
    val height: Double = 0.0, // Default value: 0.0
    val depth: Double = 0.0 // Default value: 0.0
)

data class Review(
    val rating: Int = 0, // Default value: 0
    val comment: String = "", // Default value: empty string
    val date: String = "", // Default value: empty string
    val reviewerName: String = "", // Default value: empty string
    val reviewerEmail: String = "" // Default value: empty string
)

data class Meta(
    val createdAt: String = "", // Default value: empty string
    val updatedAt: String = "", // Default value: empty string
    val barcode: String = "", // Default value: empty string
    val qrCode: String = "" // Default value: empty string
)