package com.example.smartgrocerylist.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.smartgrocerylist.ui.screens.AddItemScreen
import com.example.smartgrocerylist.ui.screens.HomeScreen
import com.example.smartgrocerylist.viewmodel.GroceryViewModel

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object AddItem : Screen("add_item")
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
                onNavigateToAddItem = { navController.navigate(Screen.AddItem.route) }
            )
        }
        composable(route = Screen.AddItem.route) {
            AddItemScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}




















//package com.example.smartgrocerylist.navigation
//
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import com.example.smartgrocerylist.ui.screens.AddItemScreen
//import com.example.smartgrocerylist.ui.screens.HomeScreen
//import com.example.smartgrocerylist.viewmodel.GroceryViewModel
//
//sealed class Screen(val route: String) {
//    data object Home : Screen("home")
//    data object AddItem : Screen("add_item")
//}
//
//@Composable
//fun NavGraph(
//    navController: NavHostController,
//    viewModel: GroceryViewModel,
//    modifier: Modifier = Modifier
//) {
//    NavHost(
//        navController = navController,
//        startDestination = Screen.Home.route,
//        modifier = modifier
//    ) {
//        composable(route = Screen.Home.route) {
//            HomeScreen(
//                viewModel = viewModel,
//                onNavigateToAddItem = { navController.navigate(Screen.AddItem.route) }
//            )
//        }
//        composable(route = Screen.AddItem.route) {
//            AddItemScreen(
//                viewModel = viewModel,
//                onBack = { navController.popBackStack() }
//            )
//        }
//    }
//}
