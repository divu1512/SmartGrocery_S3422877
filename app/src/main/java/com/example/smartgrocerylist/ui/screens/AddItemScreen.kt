package com.example.smartgrocerylist.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.smartgrocerylist.data.database.GroceryItem
import com.example.smartgrocerylist.viewmodel.GroceryViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemScreen(
    viewModel: GroceryViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var itemName by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var expirationDate by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val showDatePicker = {
        DatePickerDialog(
            context,
            { _, year, month, day ->
                calendar.set(year, month, day)
                expirationDate = dateFormatter.format(calendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Add Grocery Item") },
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
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = itemName,
                onValueChange = { itemName = it },
                label = { Text("Item Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category (e.g., Dairy, Fruits)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = expirationDate,
                onValueChange = {},
                readOnly = true,
                label = { Text("Expiration Date") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = showDatePicker) {
                        Icon(Icons.Default.DateRange, contentDescription = "Select date")
                    }
                }
            )

            Button(
                onClick = {
                    if (itemName.isNotBlank() && category.isNotBlank()) {
                        val item = GroceryItem(
                            name = itemName,
                            category = category,
                            expirationDate = expirationDate
                        )
                        viewModel.addItem(item)
                        onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Item")
            }
        }
    }
}