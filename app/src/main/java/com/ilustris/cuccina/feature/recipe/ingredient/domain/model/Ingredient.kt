package com.ilustris.cuccina.feature.recipe.ingredient.domain.model

data class Ingredient(val name: String, val quantity: Int, val type: IngredientType)

enum class IngredientType(val description: String, val abreviation: String) {
    POUNDS("Grama", "g"),
    UNITY("Unidade", "uni"),
    KILOGRAMS("Kilo", "kg"),
    LITERS("Litro", "l"),
    MILLILITERS("Mililitro", "ml"),
    SOUP("Colher de sopa", "cs"),
    TEA("Colher de chá", "chá"),
    DESSERT("Colher de sobremesa", "csb"),
    TEAPOT("Xícara e chá", "xíc"),
    CUPCOFFE("Xícara de café", "xcf"),
    CUP("Copo", "cp"),
    PIECE("Peça","pç"),
    PACKAGE("Pacote", "pc"),
    POT("Xícara", "x"),
    CENTIMETERS("Centímetros", "cm"),
    DECILITER("Decilitro", "dl"),
    CENTILITER("Centilitro", "cl")

}
