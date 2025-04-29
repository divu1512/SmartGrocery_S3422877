package com.example.smartgrocerylist.ui.screens

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.smartgrocerylist.data.model.ProductResponse
import com.example.smartgrocerylist.viewmodel.GroceryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    navController: NavController,
    productId: String,
    viewModel: GroceryViewModel
) {
    LocalContext.current
    var productResponse by remember { mutableStateOf<ProductResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(productId) {
        if (productId.isEmpty()) {
            errorMessage = "Invalid product ID."
            isLoading = false
            return@LaunchedEffect
        }

        isLoading = true
        viewModel.fetchProduct(productId) { response ->
            isLoading = false
            if (response == null || response.status == 0) {
                errorMessage = "Product not found."
            } else {
                productResponse = response
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Product Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    productResponse?.let { response ->
                        val product = response.product
                        if (product != null) {
                            Text(text = "Product Name: ${product.productName ?: "Unknown"}")
                            Text(text = "Nutrition Grade: ${product.nutritionGrades ?: "N/A"}")
                            Text(text = "Carbohydrates: ${product.nutriments?.carbohydrates ?: 0f}g")
                            Text(text = "Sugars: ${product.nutriments?.sugars ?: 0f}g")
                            Text(text = "Energy: ${product.nutriments?.energy ?: 0f} kcal")
                        }
                    }
                    if (errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    )
}