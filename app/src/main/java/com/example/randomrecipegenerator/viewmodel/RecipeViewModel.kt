package com.example.randomrecipegenerator.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomrecipegenerator.model.Recipe
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
        data class Error(val message: String) : RecipeUiState()
        data object Initial : RecipeUiState()
    }

    var uiState: RecipeUiState by mutableStateOf(RecipeUiState.Initial)
        private set

    fun fetchRandomRecipe() {
        viewModelScope.launch {
            uiState = RecipeUiState.Loading
            try {
                // Switch to the IO dispatcher for the network call
                val response = withContext(Dispatchers.IO) {
                    RecipeRetrofitClient.api.getRandomRecipe()
                }
                val recipe = response.meals?.firstOrNull()
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
}