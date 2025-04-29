package com.example.smartgrocerylist.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.smartgrocerylist.ui.screens.AddItemScreen
import com.example.smartgrocerylist.ui.screens.BarcodeScannerScreen
import com.example.smartgrocerylist.ui.screens.EditGroceryItemScreen
import com.example.smartgrocerylist.ui.screens.GroceryItemScreen
import com.example.smartgrocerylist.ui.screens.HomeScreen
import com.example.smartgrocerylist.ui.screens.ProductDetailScreen
import com.example.smartgrocerylist.viewmodel.GroceryViewModel

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object AddItem : Screen("add_item")
    data object BarcodeScanner : Screen("barcode_scanner")
    data object GroceryItem : Screen("grocery_item")
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
                navController = navController,
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
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("scanned_barcode", barcode)
                    navController.popBackStack() // go back to HomeScreen
                }
            )
        }

        composable(route = Screen.GroceryItem.route) {
            GroceryItemScreen(
                viewModel = viewModel,
                navController = navController,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "edit_item/{id}",
            arguments = listOf(navArgument("id") { defaultValue = -1 })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("id") ?: -1
            EditGroceryItemScreen(
                itemId = itemId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "product_detail_screen/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            if (productId.isNullOrEmpty()) {
                return@composable
            }
            ProductDetailScreen(navController = navController, productId = productId, viewModel = viewModel)
        }
    }
}