package com.example.smartgrocerylist.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartgrocerylist.data.database.GroceryItem
import com.example.smartgrocerylist.viewmodel.GroceryViewModel

@Composable
fun AddItemScreen(
    viewModel: GroceryViewModel,
    onBack: () -> Unit
) {
    var itemName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = itemName,
            onValueChange = { itemName = it },
            label = { Text("Enter Item Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (itemName.isNotBlank()) {
                    val newItem = GroceryItem(
                        name = itemName
                    )
                    viewModel.addItem(newItem)
                    onBack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Add Item")
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
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import com.example.smartgrocerylist.viewmodel.GroceryViewModel
//
//@Composable
//fun AddItemScreen(
//    viewModel: GroceryViewModel,
//    onBack: () -> Unit
//) {
//    var itemName by remember { mutableStateOf("") }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        TextField(
//            value = itemName,
//            onValueChange = { itemName = it },
//            label = { Text("Enter Item Name") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(
//            onClick = {
//                // Here you can add the item to the database
//                onBack()
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text(text = "Add Item")
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun AddItemScreenPreview() {
//    AddItemScreen(viewModel = GroceryViewModel()) { }
//}
