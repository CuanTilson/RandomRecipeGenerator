package com.example.randomrecipegenerator.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.randomrecipegenerator.R
import com.example.randomrecipegenerator.model.Recipe
import com.example.randomrecipegenerator.viewmodel.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(viewModel: RecipeViewModel, navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.instructions)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, stringResource(R.string.back))
                    }
                },
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
            state = listState
        ) {
            item {
                when (val state = viewModel.uiState) {
                    is RecipeViewModel.RecipeUiState.Success -> {
                        // Display the recipe title
                        Text(
                            text = state.recipe.title ?: stringResource(R.string.no_title_available),
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        // Display the image using Coil
                        Image(
                            painter = rememberAsyncImagePainter(state.recipe.image),
                            contentDescription = stringResource(R.string.recipe_image_description),
                            modifier = Modifier.size(200.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        // Display the ingredients list
                        IngredientsList(recipe = state.recipe)
                        Spacer(modifier = Modifier.height(16.dp))
                        // Display the instructions
                        Instructions(instructions = state.recipe.instructions)
                    }

                    else -> {
                        Text(stringResource(R.string.no_instructions))
                    }
                }
            }
        }
    }
}

@Composable
fun IngredientsList(recipe: Recipe) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.ingredients),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        val ingredients = listOf(
            recipe.ingredient1 to recipe.measure1,
            recipe.ingredient2 to recipe.measure2,
            recipe.ingredient3 to recipe.measure3,
            recipe.ingredient4 to recipe.measure4,
            recipe.ingredient5 to recipe.measure5,
            recipe.ingredient6 to recipe.measure6,
            recipe.ingredient7 to recipe.measure7,
            recipe.ingredient8 to recipe.measure8,
            recipe.ingredient9 to recipe.measure9,
            recipe.ingredient10 to recipe.measure10,
            recipe.ingredient11 to recipe.measure11,
            recipe.ingredient12 to recipe.measure12,
            recipe.ingredient13 to recipe.measure13,
            recipe.ingredient14 to recipe.measure14,
            recipe.ingredient15 to recipe.measure15,
            recipe.ingredient16 to recipe.measure16,
            recipe.ingredient17 to recipe.measure17,
            recipe.ingredient18 to recipe.measure18,
            recipe.ingredient19 to recipe.measure19,
            recipe.ingredient20 to recipe.measure20
        )

        ingredients.forEach { (ingredient, measure) ->
            if (!ingredient.isNullOrBlank() && !measure.isNullOrBlank()) {
                Text(text = "$ingredient - $measure")
            } else if (!ingredient.isNullOrBlank()) {
                Text(text = ingredient)
            }
        }
    }
}

@Composable
fun Instructions(instructions: String?) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.method),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        if (instructions != null) {
            val steps = instructions.split(Regex("\\r?\\n\\r?\\n"))
            steps.forEachIndexed { index, step ->
                Text(
                    text = "${index + 1}. $step",
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        } else {
            Text(text = stringResource(R.string.no_instructions_available))
        }
    }
}