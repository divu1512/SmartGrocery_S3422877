package com.example.smartgrocerylist.data.model

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("code") val code: String,
    @SerializedName("product") val product: Product?,
    @SerializedName("status") val status: Int,
    @SerializedName("status_verbose") val statusVerbose: String
)

data class Product(
    @SerializedName("product_name") val productName: String?,
    @SerializedName("nutrition_grades") val nutritionGrades: String?,
    @SerializedName("nutriments") val nutriments: Nutriments?
)

data class Nutriments(
    @SerializedName("carbohydrates") val carbohydrates: Float?,
    @SerializedName("sugars") val sugars: Float?,
    @SerializedName("energy") val energy: Float?
)
