package com.example.smartgrocerylist.ui.screens

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.smartgrocerylist.data.database.GroceryItem
import com.example.smartgrocerylist.viewmodel.GroceryViewModel

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
