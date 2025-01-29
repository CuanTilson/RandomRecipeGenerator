package com.example.randomrecipegenerator.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.randomrecipegenerator.RandomRecipeGeneratorTheme
import com.example.randomrecipegenerator.viewmodel.RecipeViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: RecipeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RandomRecipeGeneratorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "recipe") {
                        composable("recipe") {
                            RecipeScreen(viewModel, navController)
                        }
                        composable("info") {
                            InfoScreen(viewModel, navController)
                        }
                        composable("loading") {
                            LoadingScreen()
                        }
                        composable("error") {
                            ErrorScreen(message = "Error")
                        }
                        composable("history") {
                            HistoryScreen(viewModel, navController)
                        }
                    }
                }
            }
        }
    }
}