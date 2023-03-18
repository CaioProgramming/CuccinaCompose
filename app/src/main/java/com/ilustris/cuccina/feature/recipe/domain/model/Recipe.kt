package com.ilustris.cuccina.feature.recipe.domain.model

import android.os.Parcelable
import com.ilustris.cuccina.feature.recipe.ingredient.domain.model.Ingredient
import com.silent.ilustriscore.core.bean.BaseBean

data class Recipe(
    override var id: String,
    val name: String,
    val photo: String,
    val description: String,
    val time: Long,
    val portions: Int,
    val author: String,
    val ingredients: List<Ingredient>,
    val likes: List<String>
) : BaseBean(id)
