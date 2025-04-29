package com.example.smartgrocerylist.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.smartgrocerylist.data.model.ProductResponse
import com.example.smartgrocerylist.data.model.Product
import com.example.smartgrocerylist.navigation.Screen
import com.example.smartgrocerylist.viewmodel.GroceryViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: GroceryViewModel = viewModel(),
    navController: NavController = rememberNavController(),
    onNavigateToAddItem: () -> Unit
) {
    val context = LocalContext.current

    var barcode by remember { mutableStateOf(TextFieldValue("")) }
    var productName by remember { mutableStateOf(TextFieldValue("")) }
    var productResponse by remember { mutableStateOf<ProductResponse?>(null) }
    var productList by remember { mutableStateOf<List<Product>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val groceryList by viewModel.groceryList.observeAsState(emptyList())

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val scannedBarcode = savedStateHandle?.get<String>("scanned_barcode")

    var showMenu by remember { mutableStateOf(false) }

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
                savedStateHandle.remove<String>("scanned_barcode")
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Smart Grocery") },
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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = barcode,
                onValueChange = { barcode = it },
                label = { Text("Enter Barcode") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = productName,
                onValueChange = { productName = it },
                label = { Text("Search by Product Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
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
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Search Barcode")
                }

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
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Search Name")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = { navController.navigate(Screen.BarcodeScanner.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Scan Barcode with Camera")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (errorMessage.isNotEmpty()) {
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
                Text(
                    text = "Search Results",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                productList.forEach { product ->
                    ProductItemCard(product)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Grocery List",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 8.dp)
            )

            if (groceryList.isEmpty()) {
                Text("No items yet. Add some!", style = MaterialTheme.typography.bodyMedium)
            } else {
                groceryList.forEach { item ->
                    GroceryItemRow(item = item, viewModel = viewModel)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onNavigateToAddItem,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Go to Add Item Screen")
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
















//package com.example.smartgrocerylist.ui.screens
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.runtime.livedata.observeAsState
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.input.TextFieldValue
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import androidx.navigation.compose.rememberNavController
//import com.example.smartgrocerylist.data.model.ProductResponse
//import com.example.smartgrocerylist.data.model.Product
//import com.example.smartgrocerylist.navigation.Screen
//import com.example.smartgrocerylist.viewmodel.GroceryViewModel
//
//@Composable
//fun HomeScreen(
//    viewModel: GroceryViewModel = viewModel(),
//    navController: NavController = rememberNavController(),
//    onNavigateToAddItem: () -> Unit
//) {
//    var barcode by remember { mutableStateOf(TextFieldValue("")) }
//    var productName by remember { mutableStateOf(TextFieldValue("")) }
//    var productResponse by remember { mutableStateOf<ProductResponse?>(null) }
//    var productList by remember { mutableStateOf<List<Product>>(emptyList()) }
//    var isLoading by remember { mutableStateOf(false) }
//    var errorMessage by remember { mutableStateOf("") }
//    val groceryList by viewModel.groceryList.observeAsState(emptyList())
//
//    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
//    val scannedBarcode = savedStateHandle?.get<String>("scanned_barcode")
//
//    LaunchedEffect(scannedBarcode) {
//        scannedBarcode?.let {
//            barcode = TextFieldValue(it)
//            isLoading = true
//            errorMessage = ""
//            viewModel.fetchProduct(it) { response ->
//                productResponse = response
//                isLoading = false
//                if (response == null || response.status == 0) {
//                    errorMessage = "Product not found. Please try another barcode."
//                } else {
//                    productList = emptyList()
//                }
//                savedStateHandle.remove<String>("scanned_barcode")
//            }
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//            .verticalScroll(rememberScrollState()),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = "Smart Grocery",
//            style = MaterialTheme.typography.headlineLarge,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//
//        OutlinedTextField(
//            value = barcode,
//            onValueChange = { barcode = it },
//            label = { Text("Enter Barcode") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        OutlinedTextField(
//            value = productName,
//            onValueChange = { productName = it },
//            label = { Text("Search by Product Name") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            Button(
//                onClick = {
//                    isLoading = true
//                    errorMessage = ""
//                    viewModel.fetchProduct(barcode.text) { response ->
//                        productResponse = response
//                        isLoading = false
//                        if (response == null || response.status == 0) {
//                            errorMessage = "Product not found. Please try another barcode."
//                        } else {
//                            productList = emptyList()
//                        }
//                    }
//                },
//                modifier = Modifier.weight(1f)
//            ) {
//                Text("Search Barcode")
//            }
//
//            Button(
//                onClick = {
//                    isLoading = true
//                    errorMessage = ""
//                    viewModel.searchProductByName(productName.text) { response ->
//                        isLoading = false
//                        if (response != null && response.products.isNotEmpty()) {
//                            productList = response.products
//                            productResponse = null
//                        } else {
//                            errorMessage = "No products found for the name."
//                            productList = emptyList()
//                        }
//                    }
//                },
//                modifier = Modifier.weight(1f)
//            ) {
//                Text("Search Name")
//            }
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        OutlinedButton(
//            onClick = { navController.navigate(Screen.BarcodeScanner.route) },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Scan Barcode with Camera")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        if (isLoading) {
//            CircularProgressIndicator()
//            Spacer(modifier = Modifier.height(16.dp))
//        }
//
//        if (errorMessage.isNotEmpty()) {
//            Text(
//                text = errorMessage,
//                color = MaterialTheme.colorScheme.error,
//                modifier = Modifier.padding(vertical = 8.dp)
//            )
//        }
//
//        productResponse?.let { response ->
//            if (response.status == 1 && response.product != null) {
//                ProductInfoCard(response)
//                Spacer(modifier = Modifier.height(8.dp))
//            }
//        }
//
//        if (productList.isNotEmpty()) {
//            Text(
//                text = "Search Results",
//                style = MaterialTheme.typography.titleMedium,
//                modifier = Modifier.padding(vertical = 8.dp)
//            )
//            productList.forEach { product ->
//                ProductItemCard(product)
//                Spacer(modifier = Modifier.height(8.dp))
//            }
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        Text(
//            text = "Grocery List",
//            style = MaterialTheme.typography.headlineSmall,
//            modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp)
//        )
//
//        if (groceryList.isEmpty()) {
//            Text("No items yet. Add some!", style = MaterialTheme.typography.bodyMedium)
//        } else {
//            groceryList.forEach { item ->
//                GroceryItemRow(item = item, viewModel = viewModel)
//                Spacer(modifier = Modifier.height(8.dp))
//            }
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
//
//        Spacer(modifier = Modifier.height(32.dp))
//    }
//}
