package com.amg.recipesapp

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("load_image")
fun loadImage(image: ImageView, id: Int?) {
    if (id != null) {
        val suffix = "-240x150.jpg"
        val url = "https://spoonacular.com/recipeImages/$id$suffix"
        Glide.with(image.context).load(url).into(image)
    } else image.visibility = View.GONE
}