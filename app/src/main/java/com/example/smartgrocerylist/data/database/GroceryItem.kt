package com.example.smartgrocerylist.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "grocery_items")
data class GroceryItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String = "No description",
    val imageUrl: String = "",
    val details: String = "",
    val quantity: Int = 1,
    val barcode: String = "",     // Providing default value
    val brand: String = "",       // Providing default value
    val category: String = ""     // Providing default value
)







//package com.example.smartgrocerylist.data.database
//
//import androidx.room.Entity
//import androidx.room.PrimaryKey
//
//@Entity(tableName = "grocery_items")
//data class GroceryItem(
//    @PrimaryKey(autoGenerate = true) val id: Int = 0,
//    val name: String,
//    val brand: String?,
//    val category: String?,
//    val barcode: String?,
//    val description: String = "No description",
//    val imageUrl: String = "",
//    val details: String = "",
//    val quantity: Int = 1,
//)
