package com.example.smartgrocerylist.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GroceryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: GroceryItem)

    @Query("DELETE FROM grocery_items WHERE id = :id")
    suspend fun deleteItem(id: Int)

    @Query("SELECT * FROM grocery_items ORDER BY id DESC")
    fun getAllItems(): Flow<List<GroceryItem>>

    @Update
    suspend fun updateItem(item: GroceryItem)
}