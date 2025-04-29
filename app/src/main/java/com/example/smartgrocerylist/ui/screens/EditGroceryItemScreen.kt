package com.example.smartgrocerylist.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartgrocerylist.viewmodel.GroceryViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGroceryItemScreen(
    itemId: Int,
    viewModel: GroceryViewModel = viewModel(),
    onBack: () -> Unit
) {
    val itemToEdit = viewModel.groceryList.value?.find { it.id == itemId }
    var name by remember { mutableStateOf(itemToEdit?.name ?: "") }
    var category by remember { mutableStateOf(itemToEdit?.category ?: "") }
    var expiration by remember { mutableStateOf(itemToEdit?.expirationDate ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Item") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = expiration,
                    onValueChange = { expiration = it },
                    label = { Text("Expiration Date") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        if (itemToEdit != null) {
                            val updatedItem = itemToEdit.copy(
                                name = name,
                                category = category,
                                expirationDate = expiration
                            )
                            viewModel.updateItem(updatedItem)
                            onBack()
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Save")
                }
            }
        }
    )
}
