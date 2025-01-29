package com.example.randomrecipegenerator.model

import com.google.gson.annotations.SerializedName

data class Recipe(
    @SerializedName("strMeal") val title: String?,
    @SerializedName("strMealThumb") val image: String?,
    @SerializedName("strInstructions") val instructions: String?
)