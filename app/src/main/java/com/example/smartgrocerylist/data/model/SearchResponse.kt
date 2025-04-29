package com.example.smartgrocerylist.data.model

data class SearchResponse(
    val products: List<Product> = emptyList(),
    val count: Int = 0,
    val page: Int = 1
)
