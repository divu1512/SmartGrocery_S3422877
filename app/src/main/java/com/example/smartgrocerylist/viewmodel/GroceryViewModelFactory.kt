package com.example.smartgrocerylist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.smartgrocerylist.data.database.GroceryDao

class GroceryViewModelFactory(private val groceryDao: GroceryDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GroceryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GroceryViewModel(groceryDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
