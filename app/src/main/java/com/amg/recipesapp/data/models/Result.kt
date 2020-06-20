package com.amg.recipesapp.data.models

data class Result(
    val offset: Int,
    val number: Int,
    val results: MutableList<Recipe>
)