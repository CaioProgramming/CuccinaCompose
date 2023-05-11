package com.ilustris.cuccina.feature.recipe.ingredient.domain.model

data class Ingredient(
    val name: String = "",
    val quantity: Int = 0,
    val type: IngredientType = IngredientType.POUNDS
)

enum class IngredientType(val description: String, val abreviation: String, val texture: Texture) {
    POUNDS("Grama(s)", "g", Texture.POUND),
    UNITY("Unidade(s)", "uni", Texture.UNIT),
    KILOGRAMS("Kilo(s)", "kg", Texture.UNIT),
    LITERS("Litro(s)", "l", Texture.UNIT),
    MILLILITERS("Mililitro(s)", "ml", Texture.LIQUID),
    SOUP("Colher(es) de sopa", "cs", Texture.UNIT),
    TEA("Colher(es) de chá", "chá", Texture.UNIT),
    DESSERT("Colher(es) de sobremesa", "csb", Texture.LIQUID),
    TEAPOT("Xícara(s) de chá", "xíc", Texture.UNIT),
    CUPCOFFE("Xícara de café", "xcf", Texture.UNIT),
    CUP("Copo(s)", "cp", Texture.UNIT),
    PIECE("Peça(s)", "pç", Texture.UNIT),
    PACKAGE("Pacote(s)", "pc", Texture.UNIT),
    POT("Xícara(s)", "x", Texture.UNIT),
    PINCH("Pitada(s)", "pt", Texture.UNIT),
    CENTIMETERS("Centímetro(s)", "cm", Texture.UNIT),
    DECILITER("Decilitro(s)", "dl", Texture.LIQUID),
    CENTILITER("Centilitro(s)", "cl", Texture.LIQUID),
    TASTE("A gosto", "", Texture.UNDEFINED),
    SHOT("Dose(s)", "d", Texture.UNIT)

}

enum class Texture {
    UNIT, POUND, LIQUID, UNDEFINED
}
