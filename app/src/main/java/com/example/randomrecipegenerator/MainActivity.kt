package com.example.randomrecipegenerator

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.io.IOException

// Data Model
data class Recipe(
    @SerializedName("strMeal") val title: String,
    @SerializedName("strMealThumb") val image: String,
    @SerializedName("strInstructions") val instructions: String
)

// Use a more descriptive name for the API response type
data class RecipeApiResponse(val meals: List<Recipe>?)

// API Service
interface RecipeApiService {
    @GET("random.php")
    suspend fun getRandomRecipe(): RecipeApiResponse
}

// Use a more descriptive name for the Retrofit instance
object RecipeRetrofitClient {
    private const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"

    val api: RecipeApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RecipeApiService::class.java)
    }
}

// ViewModel
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

    fun fetchRandomRecipe(context: Context) {
        viewModelScope.launch {
            uiState = RecipeUiState.Loading

            // Check if internet is available before making API call
            if (!isInternetAvailable(context)) {
                uiState = RecipeUiState.Error("No internet connection. Please check your network.")
                return@launch
            }

            try {
                val response = withContext(Dispatchers.IO) {
                    RecipeRetrofitClient.api.getRandomRecipe()
                }
                val recipe = response.meals?.firstOrNull()
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

// UI Composable
@Composable
fun RecipeScreen(viewModel: RecipeViewModel, navController: NavController) {
    val context = LocalContext.current // Get context for network check

    LaunchedEffect(key1 = true) {
        viewModel.fetchRandomRecipe(context)
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
                CircularProgressIndicator()
            }

            is RecipeViewModel.RecipeUiState.Error -> {
                Text(state.message)
            }

            is RecipeViewModel.RecipeUiState.Success -> {
                Text(state.recipe.title, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { navController.navigate("info") }) {
                    Text(stringResource(R.string.view_instructions))
                }
            }

            is RecipeViewModel.RecipeUiState.Initial -> {
                Text(stringResource(R.string.press_button))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { viewModel.fetchRandomRecipe(context) }) {
            Text(stringResource(R.string.get_random_recipe))
        }
    }
}


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
                Text(state.recipe.instructions)
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

@Composable
fun AppNavigation(viewModel: RecipeViewModel) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "home") {
        composable("home") { RecipeScreen(viewModel, navController) }
        composable("info") { InfoScreen(viewModel, navController) }
    }
}

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

// Main Activity
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