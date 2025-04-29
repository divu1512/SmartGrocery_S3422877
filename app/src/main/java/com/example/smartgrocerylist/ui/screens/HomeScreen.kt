package com.example.smartgrocerylist.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartgrocerylist.data.database.GroceryItem
import com.example.smartgrocerylist.data.model.ProductResponse
import com.example.smartgrocerylist.viewmodel.GroceryViewModel

@Composable
fun HomeScreen(
    viewModel: GroceryViewModel = viewModel(),
    onNavigateToAddItem: () -> Unit
) {
    var barcode by remember { mutableStateOf(TextFieldValue("")) }
    var productResponse by remember { mutableStateOf<ProductResponse?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val groceryList by viewModel.groceryList.observeAsState(emptyList())  // Observe grocery list

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = barcode,
            onValueChange = { barcode = it },
            label = { Text("Enter Barcode") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                isLoading = true
                errorMessage = ""
                viewModel.fetchProduct(barcode.text) { response ->
                    productResponse = response
                    isLoading = false

                    if (response == null || response.status == 0) {
                        errorMessage = "Product not found. Please try another barcode."
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Search Product")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
        }

        productResponse?.let { response ->
            if (response.status == 1 && response.product != null) {
                ProductInfoCard(response)
            }
        }

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Grocery List", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(8.dp))

        groceryList.forEach { item ->
            GroceryItemRow(item = item, viewModel = viewModel)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onNavigateToAddItem,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Go to Add Item Screen")
        }
    }
}

@Composable
fun ProductInfoCard(response: ProductResponse) {
    val product = response.product

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Product Name: ${product?.productName ?: "Unknown"}")
            Text(text = "Nutrition Grade: ${product?.nutritionGrades ?: "N/A"}")
            Text(text = "Carbohydrates: ${product?.nutriments?.carbohydrates ?: 0f}g")
            Text(text = "Sugars: ${product?.nutriments?.sugars ?: 0f}g")
            Text(text = "Energy: ${product?.nutriments?.energy ?: 0f} kcal")
        }
    }
}

@Composable
fun GroceryItemRow(item: GroceryItem, viewModel: GroceryViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = item.name)

        Button(onClick = { viewModel.deleteItem(item.id) }) {
            Text(text = "Delete")
        }
    }
}




















//package com.example.smartgrocerylist.ui.screens
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.input.TextFieldValue
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.smartgrocerylist.data.model.ProductResponse
//import com.example.smartgrocerylist.viewmodel.GroceryViewModel
//
//@Composable
//fun HomeScreen(
//    viewModel: GroceryViewModel = viewModel(),
//    onNavigateToAddItem: () -> Unit
//) {
//    var barcode by remember { mutableStateOf(TextFieldValue("")) }
//    var productResponse by remember { mutableStateOf<ProductResponse?>(null) }
//    var isLoading by remember { mutableStateOf(false) }
//    var errorMessage by remember { mutableStateOf("") }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Top,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        TextField(
//            value = barcode,
//            onValueChange = { barcode = it },
//            label = { Text("Enter Barcode") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(
//            onClick = {
//                isLoading = true
//                errorMessage = ""
//                viewModel.fetchProduct(barcode.text) { response ->
//                    productResponse = response
//                    isLoading = false
//
//                    if (response == null || response.status == 0) {
//                        errorMessage = "Product not found. Please try another barcode."
//                    }
//                }
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text(text = "Search Product")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        if (isLoading) {
//            CircularProgressIndicator()
//        }
//
//        productResponse?.let { response ->
//            if (response.status == 1 && response.product != null) {
//                ProductInfoCard(response)
//            }
//        }
//
//        if (errorMessage.isNotEmpty()) {
//            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
//        }
//
//        Spacer(modifier = Modifier.height(32.dp))
//
//        Button(
//            onClick = onNavigateToAddItem,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text(text = "Go to Add Item Screen")
//        }
//    }
//}
//
//@Composable
//fun ProductInfoCard(response: ProductResponse) {
//    val product = response.product
//
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp)
//    ) {
//        Column(
//            modifier = Modifier.padding(16.dp)
//        ) {
//            Text(text = "Product Name: ${product?.productName ?: "Unknown"}")
//            Text(text = "Nutrition Grade: ${product?.nutritionGrades ?: "N/A"}")
//            Text(text = "Carbohydrates: ${product?.nutriments?.carbohydrates ?: 0f}g")
//            Text(text = "Sugars: ${product?.nutriments?.sugars ?: 0f}g")
//            Text(text = "Energy: ${product?.nutriments?.energy ?: 0f} kcal")
//        }
//    }
//}
