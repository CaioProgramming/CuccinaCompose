package com.ilustris.cuccina.feature.recipe.ingredient.domain.model

data class Ingredient(val name: String, val quantity: Int, val type: IngredientType)

enum class IngredientType(val description: String, val abreviation: String, val texture: Texture) {
    POUNDS("Grama(s)", "g", Texture.POUND),
    UNITY("Unidade(s)", "uni", Texture.UNIT),
    KILOGRAMS("Kilo(s)", "kg", Texture.POUND),
    LITERS("Litro(s)", "l", Texture.LIQUID),
    MILLILITERS("Mililitro(s)", "ml", Texture.LIQUID),
    SOUP("Colher(es) de sopa", "cs", Texture.LIQUID),
    TEA("Colher(es) de chá", "chá", Texture.LIQUID),
    DESSERT("Colher(es) de sobremesa", "csb", Texture.LIQUID),
    TEAPOT("Xícara(s) de chá", "xíc", Texture.LIQUID),
    CUPCOFFE("Xícara de café", "xcf", Texture.LIQUID),
    CUP("Copo(s)", "cp", Texture.LIQUID),
    PIECE("Peça(s)", "pç", Texture.UNIT),
    PACKAGE("Pacote(s)", "pc", Texture.UNIT),
    POT("Xícara(s)", "x", Texture.LIQUID),
    PINCH("Pitada(s)", "pt", Texture.UNIT),
    CENTIMETERS("Centímetro(s)", "cm", Texture.UNIT),
    DECILITER("Decilitro(s)", "dl", Texture.LIQUID),
    CENTILITER("Centilitro(s)", "cl", Texture.LIQUID)

}

enum class Texture {
    UNIT, POUND, LIQUID
}
