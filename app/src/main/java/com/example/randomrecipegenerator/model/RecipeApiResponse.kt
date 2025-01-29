package com.example.randomrecipegenerator.model

import com.google.gson.annotations.SerializedName

data class RecipeApiResponse(
    @SerializedName("meals") val meals: List<Recipe>?
)