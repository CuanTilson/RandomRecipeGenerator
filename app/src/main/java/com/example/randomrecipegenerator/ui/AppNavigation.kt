package com.example.randomrecipegenerator.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.randomrecipegenerator.viewmodel.RecipeViewModel

@Composable
fun AppNavigation(viewModel: RecipeViewModel) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "home") {
        composable("home") { RecipeScreen(viewModel, navController) }
        composable("info") { InfoScreen(viewModel, navController) }
    }
}