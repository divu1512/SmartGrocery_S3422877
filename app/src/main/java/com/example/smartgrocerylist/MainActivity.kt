package com.example.smartgrocerylist

import android.content.Intent
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
import com.example.smartgrocerylist.ui.screens.LoginActivity
import com.example.smartgrocerylist.ui.theme.SmartGroceryListTheme
import com.example.smartgrocerylist.viewmodel.GroceryViewModel
import com.example.smartgrocerylist.viewmodel.GroceryViewModelFactory
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: GroceryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Initialize Room Database and GroceryDao
        val database = GroceryDatabase.getDatabase(this)
        val groceryDao = database.groceryDao()

        // Initialize ViewModel using Factory
        val factory = GroceryViewModelFactory(groceryDao)
        viewModel = ViewModelProvider(this, factory)[GroceryViewModel::class.java]

        // Check if a user is logged in; if not redirect to LoginActivity
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else setContent {
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
    NavGraph(navController = navController, viewModel = viewModel)
}