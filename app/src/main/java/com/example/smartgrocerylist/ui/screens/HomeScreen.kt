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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.smartgrocerylist.data.model.ProductResponse
import com.example.smartgrocerylist.data.model.Product
import com.example.smartgrocerylist.navigation.Screen
import com.example.smartgrocerylist.viewmodel.GroceryViewModel

@Composable
fun HomeScreen(
    viewModel: GroceryViewModel = viewModel(),
    navController: NavController = rememberNavController(),
    onNavigateToAddItem: () -> Unit
) {
    var barcode by remember { mutableStateOf(TextFieldValue("")) }
    var productName by remember { mutableStateOf(TextFieldValue("")) }
    var productResponse by remember { mutableStateOf<ProductResponse?>(null) }
    var productList by remember { mutableStateOf<List<Product>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val groceryList by viewModel.groceryList.observeAsState(emptyList())

    //  Auto-trigger fetchProduct if scanned barcode is passed back
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val scannedBarcode = savedStateHandle?.get<String>("scanned_barcode")

    LaunchedEffect(scannedBarcode) {
        scannedBarcode?.let {
            barcode = TextFieldValue(it)
            isLoading = true
            errorMessage = ""
            viewModel.fetchProduct(it) { response ->
                productResponse = response
                isLoading = false

                if (response == null || response.status == 0) {
                    errorMessage = "Product not found. Please try another barcode."
                } else {
                    productList = emptyList()
                }

                //  Clear scanned value after use
                savedStateHandle.remove<String>("scanned_barcode")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Barcode input
        TextField(
            value = barcode,
            onValueChange = { barcode = it },
            label = { Text("Enter Barcode") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Product name input
        TextField(
            value = productName,
            onValueChange = { productName = it },
            label = { Text("Search by Product Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Barcode search button
        Button(
            onClick = {
                isLoading = true
                errorMessage = ""
                viewModel.fetchProduct(barcode.text) { response ->
                    productResponse = response
                    isLoading = false

                    if (response == null || response.status == 0) {
                        errorMessage = "Product not found. Please try another barcode."
                    } else {
                        productList = emptyList()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Search by Barcode")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Name search button
        Button(
            onClick = {
                isLoading = true
                errorMessage = ""
                viewModel.searchProductByName(productName.text) { response ->
                    isLoading = false
                    if (response != null && response.products.isNotEmpty()) {
                        productList = response.products
                        productResponse = null
                    } else {
                        errorMessage = "No products found for the name."
                        productList = emptyList()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Search by Name")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Scan barcode using camera
        Button(
            onClick = { navController.navigate(Screen.BarcodeScanner.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Scan Barcode with Camera")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
        }

        // Single product result
        productResponse?.let { response ->
            if (response.status == 1 && response.product != null) {
                ProductInfoCard(response)
            }
        }

        // List of products from name search
        productList.forEach { product ->
            ProductItemCard(product)
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
