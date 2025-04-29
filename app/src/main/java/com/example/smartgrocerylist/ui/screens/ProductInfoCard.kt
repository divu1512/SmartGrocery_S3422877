package com.example.smartgrocerylist.ui.screens

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp
import com.example.smartgrocerylist.data.model.ProductResponse

@Composable
fun ProductInfoCard(response: ProductResponse) {
    val product = response.product

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Product Name: ${product?.productName ?: "Unknown"}")
            Text(text = "Nutrition Grade: ${product?.nutritionGrades ?: "N/A"}")
            Text(text = "Carbohydrates: ${product?.nutriments?.carbohydrates ?: 0f}g")
            Text(text = "Sugars: ${product?.nutriments?.sugars ?: 0f}g")
            Text(text = "Energy: ${product?.nutriments?.energy ?: 0f} kcal")
        }
    }
}
