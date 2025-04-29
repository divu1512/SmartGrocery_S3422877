package com.example.smartgrocerylist.ui.screens

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.smartgrocerylist.data.model.Product
import com.example.smartgrocerylist.data.model.ProductResponse
import com.example.smartgrocerylist.navigation.Screen
import com.example.smartgrocerylist.viewmodel.GroceryViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: GroceryViewModel = viewModel(),
    navController: NavController,
    onNavigateToAddItem: () -> Unit
) {
    val context = LocalContext.current

    var searchInput by remember { mutableStateOf(TextFieldValue("")) }
    var productResponse by remember { mutableStateOf<ProductResponse?>(null) }
    var productList by remember { mutableStateOf<List<Product>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showMenu by remember { mutableStateOf(false) }

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val scannedBarcode = savedStateHandle?.get<String>("scanned_barcode")

    LaunchedEffect(scannedBarcode) {
        scannedBarcode?.let {
            searchInput = TextFieldValue(it)
            performSearch(
                viewModel = viewModel,
                input = it,
                productResponseSetter = { value -> productResponse = value },
                productListSetter = { value -> productList = value },
                isLoadingSetter = { value -> isLoading = value },
                errorMessageSetter = { value -> errorMessage = value }
            )
            savedStateHandle.remove<String>("scanned_barcode")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("\n" +
                        "\uD83D\uDED2 Smart Grocery \uD83D\uDECD\uFE0F", fontSize = 27.sp) },
                actions = {
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Logout") },
                            onClick = {
                                FirebaseAuth.getInstance().signOut()
                                showMenu = false
                                context.startActivity(Intent(context, LoginActivity::class.java))
                            }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.End
            ) {
                FloatingActionButton(
                    onClick = { navController.navigate(Screen.GroceryItem.route) },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.List, contentDescription = "View Grocery Items")
                }

                FloatingActionButton(
                    onClick = onNavigateToAddItem,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Item")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = searchInput,
                onValueChange = { searchInput = it },
                label = { Text("Enter Product Name or Barcode to see details") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    performSearch(
                        viewModel = viewModel,
                        input = searchInput.text,
                        productResponseSetter = { value -> productResponse = value },
                        productListSetter = { value -> productList = value },
                        isLoadingSetter = { value -> isLoading = value },
                        errorMessageSetter = { value -> errorMessage = value }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Search")
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = { navController.navigate(Screen.BarcodeScanner.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Scan Barcode with Camera")
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            AnimatedVisibility(visible = errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            productResponse?.let { response ->
                if (response.status == 1 && response.product != null) {
                    ProductInfoCard(response)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            if (productList.isNotEmpty()) {
                Text("Search Results", style = MaterialTheme.typography.titleMedium)
                productList.forEach { product ->
                    ProductItemCard(product = product)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

private fun performSearch(
    viewModel: GroceryViewModel,
    input: String,
    productResponseSetter: (ProductResponse?) -> Unit,
    productListSetter: (List<Product>) -> Unit,
    isLoadingSetter: (Boolean) -> Unit,
    errorMessageSetter: (String) -> Unit
) {
    isLoadingSetter(true)
    errorMessageSetter("")
    if (input.all { it.isDigit() }) {
        // Search by Barcode
        viewModel.fetchProduct(input) { response ->
            isLoadingSetter(false)
            if (response == null || response.status == 0) {
                errorMessageSetter("Product not found.")
                productResponseSetter(null)
                productListSetter(emptyList())
            } else {
                productResponseSetter(response)
                productListSetter(emptyList())
            }
        }
    } else {
        // Search by Name
        viewModel.searchProductByName(input) { response ->
            isLoadingSetter(false)
            if (response != null && response.products.isNotEmpty()) {
                val filteredProducts = response.products.filter { product ->
                    product.productName.equals(input, ignoreCase = true) || product.productName?.contains(input, ignoreCase = true) == true
                }
                if (filteredProducts.isNotEmpty()) {
                    productListSetter(filteredProducts)
                    productResponseSetter(null)
                } else {
                    errorMessageSetter("No matching products found.")
                    productListSetter(emptyList())
                }
            } else {
                errorMessageSetter("No products found.")
                productListSetter(emptyList())
            }
        }
    }
}