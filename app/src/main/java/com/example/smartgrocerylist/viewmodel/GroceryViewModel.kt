package com.example.smartgrocerylist.viewmodel

import androidx.lifecycle.*
import com.example.smartgrocerylist.data.api.RetrofitInstance
import com.example.smartgrocerylist.data.database.GroceryDao
import com.example.smartgrocerylist.data.database.GroceryItem
import com.example.smartgrocerylist.data.model.ProductResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class GroceryViewModel(private val groceryDao: GroceryDao) : ViewModel() {

    private val _groceryList = MutableLiveData<List<GroceryItem>>()
    val groceryList: LiveData<List<GroceryItem>> = _groceryList

    private fun fetchAllItems() {
        viewModelScope.launch(Dispatchers.IO) {
            groceryDao.getAllItems().collect { items ->
                _groceryList.postValue(items)
            }
        }
    }

    fun addItem(groceryItem: GroceryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            groceryDao.insertItem(groceryItem)
            fetchAllItems()
        }
    }

    fun deleteItem(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            groceryDao.deleteItem(id)
            fetchAllItems()
        }
    }

    fun fetchProduct(barcode: String, onResult: (ProductResponse?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response: Response<ProductResponse> = RetrofitInstance.api.getProductByBarcode(barcode)
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    onResult(null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(null)
            }
        }
    }
}














//package com.example.smartgrocerylist.viewmodel
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.smartgrocerylist.data.api.RetrofitInstance
//import com.example.smartgrocerylist.data.model.ProductResponse
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import retrofit2.Response
//
//class GroceryViewModel : ViewModel() {
//
//    fun fetchProduct(barcode: String, onResult: (ProductResponse?) -> Unit) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val response: Response<ProductResponse> = RetrofitInstance.api.getProductByBarcode(barcode)
//                if (response.isSuccessful) {
//                    onResult(response.body())
//                } else {
//                    onResult(null)
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//                onResult(null)
//            }
//        }
//    }
//}
