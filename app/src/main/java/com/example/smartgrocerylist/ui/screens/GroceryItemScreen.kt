package com.example.smartgrocerylist.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartgrocerylist.data.database.GroceryItem
import com.example.smartgrocerylist.viewmodel.GroceryViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroceryItemScreen(
    viewModel: GroceryViewModel = viewModel(),
    navController: NavController,
    onBack: () -> Unit
) {
    val groceryList by viewModel.groceryList.observeAsState()
    var itemToDelete by remember { mutableStateOf<GroceryItem?>(null) }

    var sortOption by remember { mutableStateOf("None") }
    var filterCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Fruits", "Vegetables", "Dairy", "Snacks", "Beverages", "Other")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Grocery Items") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (groceryList.isNullOrEmpty()) {
                Text("No items added yet.", style = MaterialTheme.typography.bodyLarge)
            } else {
                // Sort and Filter Dropdowns
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SortDropdown(sortOption) { selected ->
                        sortOption = selected
                    }
                    FilterDropdown(filterCategory, categories) { selected ->
                        filterCategory = selected
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Apply filter and sort
                var displayedItems = groceryList ?: emptyList()

                // Filter
                if (filterCategory != "All") {
                    displayedItems = displayedItems.filter { it.category.equals(filterCategory, ignoreCase = true) }
                }

                // Sort
                displayedItems = when (sortOption) {
                    "Name" -> displayedItems.sortedBy { it.name.lowercase() }
                    "Category" -> displayedItems.sortedBy { it.category.lowercase() }
                    "Expiration Date" -> displayedItems.sortedBy { it.expirationDate }
                    else -> displayedItems
                }

                // Display items
                displayedItems.forEach { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate("edit_item/${item.id}")
                            },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Name: ${item.name}", style = MaterialTheme.typography.titleMedium)
                            Text("Category: ${item.category}")
                            Text("Expires: ${item.expirationDate}")
                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            ) {
                                IconButton(onClick = {
                                    navController.navigate("edit_item/${item.id}")
                                }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                                }
                                IconButton(onClick = {
                                    itemToDelete = item
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }

    // Delete Confirmation
    if (itemToDelete != null) {
        AlertDialog(
            onDismissRequest = { itemToDelete = null },
            title = { Text("Delete Item") },
            text = { Text("Are you sure you want to delete '${itemToDelete?.name}'?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteItem(itemToDelete!!.id)
                    itemToDelete = null
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { itemToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun SortDropdown(selectedSort: String, onSortSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("None", "Name", "Category", "Expiration Date")

    Box {
        Button(onClick = { expanded = true }) {
            Text("Sort: $selectedSort")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSortSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun FilterDropdown(selectedFilter: String, categories: List<String>, onFilterSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(onClick = { expanded = true }) {
            Text("Filter: $selectedFilter")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category) },
                    onClick = {
                        onFilterSelected(category)
                        expanded = false
                    }
                )
            }
        }
    }
}