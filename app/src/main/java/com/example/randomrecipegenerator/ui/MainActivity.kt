package com.example.randomrecipegenerator.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import com.example.randomrecipegenerator.RandomRecipeGeneratorTheme
import com.example.randomrecipegenerator.viewmodel.RecipeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RandomRecipeGeneratorTheme {
                val viewModel = remember { RecipeViewModel() }
                AppNavigation(viewModel)
            }
        }
    }
}