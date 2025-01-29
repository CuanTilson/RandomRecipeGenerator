package com.example.randomrecipegenerator.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomrecipegenerator.model.Recipe
import com.example.randomrecipegenerator.model.RecipeApiResponse
import com.example.randomrecipegenerator.network.RecipeRetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class RecipeViewModel : ViewModel() {
    // Use sealed class for better state management
    sealed class RecipeUiState {
        data object Loading : RecipeUiState()
        data class Success(val recipe: Recipe) : RecipeUiState()
        data class Error(val message: String?) : RecipeUiState()
        data object Initial : RecipeUiState()
    }

    var uiState: RecipeUiState by mutableStateOf(RecipeUiState.Initial)
        private set

    // List to store previous recipes
    private val _previousRecipes = mutableListOf<Recipe>()
    val previousRecipes: List<Recipe> = _previousRecipes

    fun fetchRandomRecipe() {
        viewModelScope.launch {
            uiState = RecipeUiState.Loading
            try {
                // Switch to the IO dispatcher for the network call
                val response: RecipeApiResponse = withContext(Dispatchers.IO) {
                    RecipeRetrofitClient.api.getRandomRecipe()
                }
                // Log the entire response
                Log.d("RecipeViewModel", "API Response: $response")

                val apiRecipe = response.meals?.firstOrNull()

                val recipe = apiRecipe?.let {
                    Recipe(
                        title = it.title,
                        image = it.image,
                        instructions = it.instructions,
                        ingredient1 = it.ingredient1,
                        ingredient2 = it.ingredient2,
                        ingredient3 = it.ingredient3,
                        ingredient4 = it.ingredient4,
                        ingredient5 = it.ingredient5,
                        ingredient6 = it.ingredient6,
                        ingredient7 = it.ingredient7,
                        ingredient8 = it.ingredient8,
                        ingredient9 = it.ingredient9,
                        ingredient10 = it.ingredient10,
                        ingredient11 = it.ingredient11,
                        ingredient12 = it.ingredient12,
                        ingredient13 = it.ingredient13,
                        ingredient14 = it.ingredient14,
                        ingredient15 = it.ingredient15,
                        ingredient16 = it.ingredient16,
                        ingredient17 = it.ingredient17,
                        ingredient18 = it.ingredient18,
                        ingredient19 = it.ingredient19,
                        ingredient20 = it.ingredient20,
                        measure1 = it.measure1,
                        measure2 = it.measure2,
                        measure3 = it.measure3,
                        measure4 = it.measure4,
                        measure5 = it.measure5,
                        measure6 = it.measure6,
                        measure7 = it.measure7,
                        measure8 = it.measure8,
                        measure9 = it.measure9,
                        measure10 = it.measure10,
                        measure11 = it.measure11,
                        measure12 = it.measure12,
                        measure13 = it.measure13,
                        measure14 = it.measure14,
                        measure15 = it.measure15,
                        measure16 = it.measure16,
                        measure17 = it.measure17,
                        measure18 = it.measure18,
                        measure19 = it.measure19,
                        measure20 = it.measure20
                    )
                }
                // Add the current recipe to the history
                if (recipe != null) {
                    _previousRecipes.add(recipe)
                }
                // Lift the assignment out of the if statement
                uiState = recipe?.let { RecipeUiState.Success(it) } ?: RecipeUiState.Error("No recipe found")
            } catch (e: IOException) {
                uiState = RecipeUiState.Error("Network error: ${e.message}")
            } catch (e: HttpException) {
                uiState = RecipeUiState.Error("HTTP error: ${e.message}")
            } catch (e: Exception) {
                uiState = RecipeUiState.Error("Failed to fetch recipe: ${e.message}")
            }
        }
    }
    fun setRecipe(recipe: Recipe) {
        uiState = RecipeUiState.Success(recipe)
    }
}