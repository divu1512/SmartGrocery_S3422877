package com.example.smartgrocerylist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.smartgrocerylist.data.database.GroceryDatabase
import com.example.smartgrocerylist.navigation.NavGraph
import com.example.smartgrocerylist.ui.theme.SmartGroceryListTheme
import com.example.smartgrocerylist.viewmodel.GroceryViewModel
import com.example.smartgrocerylist.viewmodel.GroceryViewModelFactory

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: GroceryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Room Database and GroceryDao
        val database = GroceryDatabase.getDatabase(this)
        val groceryDao = database.groceryDao()

        // Initialize ViewModel using Factory
        val factory = GroceryViewModelFactory(groceryDao)
        viewModel = ViewModelProvider(this, factory)[GroceryViewModel::class.java]

        setContent {
            SmartGroceryListTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    GroceryApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun GroceryApp(viewModel: GroceryViewModel) {
    val navController = rememberNavController()
    NavGraph(navController = navController, viewModel = viewModel) // <-- Fixed Parameter Name
}













//package com.example.smartgrocerylist
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.viewModels
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.runtime.Composable
//import androidx.navigation.compose.rememberNavController
//import com.example.smartgrocerylist.navigation.NavGraph
//import com.example.smartgrocerylist.ui.theme.SmartGroceryListTheme
//import com.example.smartgrocerylist.viewmodel.GroceryViewModel
//
//class MainActivity : ComponentActivity() {
//    private val viewModel: GroceryViewModel by viewModels() // ViewModel Initialization
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            SmartGroceryListTheme {
//                Surface(color = MaterialTheme.colorScheme.background) {
//                    GroceryApp(viewModel)
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun GroceryApp(viewModel: GroceryViewModel) {
//    val navController = rememberNavController()
//    NavGraph(navController = navController, viewModel = viewModel)
//}
