package com.example.randomrecipegenerator.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.randomrecipegenerator.R
import com.example.randomrecipegenerator.viewmodel.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeScreen(viewModel: RecipeViewModel, navController: NavController) {
    // Trigger initial fetch on first composition
    LaunchedEffect(key1 = true) {
        viewModel.fetchRandomRecipe()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        val listState = rememberLazyListState()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            state = listState
        ) {
            item {
                when (val state = viewModel.uiState) {
                    is RecipeViewModel.RecipeUiState.Loading -> {
                        LoadingScreen()
                    }

                    is RecipeViewModel.RecipeUiState.Error -> {
                        ErrorScreen(message = state.message)
                    }

                    is RecipeViewModel.RecipeUiState.Success -> {
                        // Check if title is null and provide a default value
                        Text(
                            text = state.recipe.title ?: stringResource(R.string.no_title_available),
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
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
    }
}