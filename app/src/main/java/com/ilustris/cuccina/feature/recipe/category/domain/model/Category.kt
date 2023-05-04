package com.ilustris.cuccina.feature.recipe.category.domain.model

import com.ilustris.cuccina.R

enum class Category(val icon: Int = R.drawable.cherry, val title: String, val description: String = "") {

    DRINKS(title = "Bebidas", icon = R.drawable.cocktail),
    CANDY(title = "Confeitaria e doces", icon = R.drawable.candy),
    SOUPS(title = "Sopas e cremes", icon = R.drawable.soup),
    SEA(title = "Frutos do mar", icon = R.drawable.crab),
    VEGGIES(title = "Legumes e vegetais", icon = R.drawable.vegetable),
    PASTA(title = "Massas", icon = R.drawable.pasta),
    SAUCE(title = "Molhos", icon = R.drawable.sauce),
    PROTEIN(title = "Proteínas", icon = R.drawable.meat),
    SALADS(title = "Saladas", icon = R.drawable.salad),
    UNKNOW(title = "Outros", icon = R.drawable.cherry)
}
