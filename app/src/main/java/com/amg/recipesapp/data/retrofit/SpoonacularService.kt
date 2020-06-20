package com.amg.recipesapp.data.retrofit

import com.amg.recipesapp.data.models.Result
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SpoonacularService {
    @GET("recipes/search?apiKey=45f9d2b896294e1da2faa7fce3d23ed9&limitLicense=true&instructionsRequired=true&number=1")
    fun getRecipes(
        @Query("query") query: String,
        @Query("cuisine") cuisine: String,
        @Query("diet") diet: String,
        @Query("excludeIngredients") excludeIngredients: String
    ): Call<Result>
}