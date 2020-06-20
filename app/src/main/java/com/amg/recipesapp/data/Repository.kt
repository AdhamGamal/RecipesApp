package com.amg.recipesapp.data

import androidx.lifecycle.MutableLiveData
import com.amg.recipesapp.data.models.Recipe
import javax.inject.Inject
import com.amg.recipesapp.data.models.Result
import com.amg.recipesapp.data.retrofit.SpoonacularService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository @Inject constructor(private val service: SpoonacularService) {

    val recipe = MutableLiveData<Recipe>(null)
    val error = MutableLiveData<String>(null)

    fun getRecipes(answers: MutableList<String>) {
        service.getRecipes(answers[0], answers[1], answers[2], answers[3])
            .enqueue(object : Callback<Result?> {
                override fun onFailure(call: Call<Result?>, exception: Throwable) {
                    error.value = "Check your network!"
                }

                override fun onResponse(call: Call<Result?>, response: Response<Result?>) {
                    val result = response.body()
                    if (result != null && result.results.size > 0) {
                        recipe.value = result.results[0]
                    }else{
                        error.value = "No Recipe Found"
                    }
                }
            })
    }
}