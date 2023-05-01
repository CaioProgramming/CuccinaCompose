package com.ilustris.cuccina.feature.recipe.form.presentation.viewstate

sealed class NewRecipeViewState {
    object Loading : NewRecipeViewState()
    object Success : NewRecipeViewState()
    object Error : NewRecipeViewState()
}
