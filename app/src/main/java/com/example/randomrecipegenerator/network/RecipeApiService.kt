package com.example.randomrecipegenerator.network

import com.example.randomrecipegenerator.model.RecipeApiResponse
import retrofit2.http.GET

interface RecipeApiService {
    @GET("random.php")
    suspend fun getRandomRecipe(): RecipeApiResponse
}