package com.example.randomrecipegenerator.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.randomrecipegenerator.R
import com.example.randomrecipegenerator.viewmodel.RecipeViewModel

@Composable
fun InfoScreen(viewModel: RecipeViewModel, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (val state = viewModel.uiState) {
            is RecipeViewModel.RecipeUiState.Success -> {
                // Display the image using Coil
                Image(
                    painter = rememberAsyncImagePainter(state.recipe.image),
                    contentDescription = stringResource(R.string.recipe_image_description),
                    modifier = Modifier.size(200.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Check if instructions is null and provide a default value
                Text(text = state.recipe.instructions ?: stringResource(R.string.no_instructions_available))
            }

            else -> {
                Text(stringResource(R.string.no_instructions))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text(stringResource(R.string.back))
        }
    }
}