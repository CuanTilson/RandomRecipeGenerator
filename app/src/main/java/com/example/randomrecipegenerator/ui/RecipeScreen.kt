package com.example.randomrecipegenerator.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.randomrecipegenerator.R
import com.example.randomrecipegenerator.viewmodel.RecipeViewModel

@Composable
fun RecipeScreen(viewModel: RecipeViewModel, navController: NavController) {
    // Trigger initial fetch on first composition
    LaunchedEffect(key1 = true) {
        viewModel.fetchRandomRecipe()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (val state = viewModel.uiState) {
            is RecipeViewModel.RecipeUiState.Loading -> {
                LoadingScreen()
            }

            is RecipeViewModel.RecipeUiState.Error -> {
                ErrorScreen(message = state.message)
            }

            is RecipeViewModel.RecipeUiState.Success -> {
                Text(state.recipe.title, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { navController.navigate("info") }) {
                    Text(stringResource(R.string.view_instructions))
                }
            }

            is RecipeViewModel.RecipeUiState.Initial -> {
                // Optionally display a message or do nothing
                Text(stringResource(R.string.press_button))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { viewModel.fetchRandomRecipe() }) {
            Text(stringResource(R.string.get_random_recipe))
        }
    }
}