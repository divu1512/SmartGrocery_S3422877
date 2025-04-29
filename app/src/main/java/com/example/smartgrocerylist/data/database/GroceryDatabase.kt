package com.example.smartgrocerylist.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GroceryItem::class], version = 2, exportSchema = false)  // Updated version from 1 to 2
abstract class GroceryDatabase : RoomDatabase() {
    abstract fun groceryDao(): GroceryDao

    companion object {
        @Volatile
        private var INSTANCE: GroceryDatabase? = null

        fun getDatabase(context: Context): GroceryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GroceryDatabase::class.java,
                    "grocery_db"
                )
                    .fallbackToDestructiveMigration()  // Clears old data and recreates the database
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}














//package com.example.smartgrocerylist.data.database
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//
//@Database(entities = [GroceryItem::class], version = 1, exportSchema = false)
//abstract class GroceryDatabase : RoomDatabase() {
//    abstract fun groceryDao(): GroceryDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: GroceryDatabase? = null
//
//        fun getDatabase(context: Context): GroceryDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    GroceryDatabase::class.java,
//                    "grocery_db"
//                ).build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}
