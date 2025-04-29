package com.example.smartgrocerylist.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.smartgrocerylist.ui.screens.AddItemScreen
import com.example.smartgrocerylist.ui.screens.BarcodeScannerScreen
import com.example.smartgrocerylist.ui.screens.HomeScreen
import com.example.smartgrocerylist.viewmodel.GroceryViewModel

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object AddItem : Screen("add_item")
    data object BarcodeScanner : Screen("barcode_scanner")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: GroceryViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                navController = navController, // pass navController
                onNavigateToAddItem = { navController.navigate(Screen.AddItem.route) }
            )
        }

        composable(route = Screen.AddItem.route) {
            AddItemScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(route = Screen.BarcodeScanner.route) {
            BarcodeScannerScreen(
                onBarcodeDetected = { barcode ->
                    //  Save barcode to previous screen
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("scanned_barcode", barcode)
                    navController.popBackStack() // go back to HomeScreen
                }
            )
        }
    }
}
