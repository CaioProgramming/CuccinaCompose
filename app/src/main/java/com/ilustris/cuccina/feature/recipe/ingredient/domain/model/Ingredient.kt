package com.ilustris.cuccina.feature.recipe.ingredient.domain.model

data class Ingredient(val name: String, val quantity: Int, val type: IngredientType)

enum class IngredientType(val description: String, val abreviation: String, val texture: Texture) {
    POUNDS("Grama(s)", "g", Texture.SOLID),
    UNITY("Unidade(s)", "uni", Texture.SOLID),
    KILOGRAMS("Kilo(s)", "kg", Texture.SOLID),
    LITERS("Litro(s)", "l", Texture.LIQUID),
    MILLILITERS("Mililitro(s)", "ml", Texture.LIQUID),
    SOUP("Colher de sopa", "cs", Texture.LIQUID),
    TEA("Colher de chá", "chá", Texture.LIQUID),
    DESSERT("Colher de sobremesa", "csb", Texture.LIQUID),
    TEAPOT("Xícara e chá", "xíc", Texture.LIQUID),
    CUPCOFFE("Xícara de café", "xcf", Texture.LIQUID),
    CUP("Copo(s)", "cp", Texture.LIQUID),
    PIECE("Peça(s)", "pç", Texture.SOLID),
    PACKAGE("Pacote(s)", "pc", Texture.SOLID),
    POT("Xícara(s)", "x", Texture.LIQUID),
    PINCH("Pitada(s)", "pt", Texture.SOLID),
    CENTIMETERS("Centímetro(s)", "cm", Texture.SOLID),
    DECILITER("Decilitro(s)", "dl", Texture.LIQUID),
    CENTILITER("Centilitro(s)", "cl", Texture.LIQUID)

}

enum class Texture {
    SOLID, LIQUID
}
