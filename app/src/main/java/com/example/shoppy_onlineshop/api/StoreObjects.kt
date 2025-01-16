package com.example.shoppy_onlineshop.api

data class StoreObjects(
    val products: List<StoreProduct>,
    val total: Int,
    val skip: Int,
    val limit: Int
)