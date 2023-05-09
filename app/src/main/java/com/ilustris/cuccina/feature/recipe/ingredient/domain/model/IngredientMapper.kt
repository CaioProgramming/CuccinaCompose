package com.ilustris.cuccina.feature.recipe.ingredient.domain.model

import android.util.Log

object IngredientMapper {

    fun getIngredientSymbol(ingredientName: String) = emojisDictionary().find {
        val ingredient = ingredientName.lowercase().trim()
        if (ingredient.isEmpty()) return "❓"
        Log.i(
            javaClass.simpleName,
            "getIngredientSymbol: searching for $ingredient in ${it.ingredientNames}"
        )
        it.ingredientNames.any { names ->
            ingredient.contains(names, true)
        }
    }?.emoji ?: "❓"

    private fun emojisDictionary() = listOf(
        EmojiDic(
            "🐟",
            listOf(
                "fish",
                "peixe",
                "salmão",
                "salmao",
                "tilapia",
                "atum",
                "bacalhau",
                "sardinha",
                "anchova",
                "truta",
                "pescada",
                "merluza",
                "tambaqui",
                "pintado",
                "dourado",
                "panga"
            )
        ),
        EmojiDic(
            "\uD83E\uDD69",
            listOf(
                "carne",
                "carne bovina",
                "carne de boi",
                "picanha",
                "bife",
                "alcatra",
                "filé mignon",
                "filé",
                "maminha",
                "coxão mole",
                "coxão duro",
                "patinho",
                "lagarto",
                "fraldinha",
                "cupim",
                "costela",
                "t bone",
                "contra filé",
            )
        ),
        EmojiDic(
            "🍗",
            listOf(
                "frango",
                "peito de frango",
                "coxa de frango",
                "coxa",
                "sobrecoxa",
                "asa",
                "asa de frango",
            )
        ),
        EmojiDic("🧄", listOf("alho", "alhos", "garlic")),
        EmojiDic("\uD83E\uDED8", listOf("feijao", "feijão", "beans")),
        EmojiDic("🥚", listOf("ovo", "ovos", "egg", "eggs")),
        EmojiDic("🧂", listOf("sal", "sal grosso", "sal refinado", "sal marinho", "sal rosa")),
        EmojiDic("🧈", listOf("manteiga", "margarina", "manteigas", "margarinas", "butter")),
        EmojiDic("🥛", listOf("leite", "leites", "milk")),
        EmojiDic("🧀", listOf("queijo", "queijos", "cheese")),
        EmojiDic("🍞", listOf("pão", "pães", "bread")),
        EmojiDic("🍖", listOf("linguiça", "linguiças", "sausage", "bisteca", "bistecas")),
        EmojiDic("🥓", listOf("bacon", "bacons")),
        EmojiDic("🥩", listOf("carne de porco", "carne suína", "bife suíno")),
        EmojiDic("🦐", listOf("camarao", "camarão", "shrimp")),
        EmojiDic("🍝", listOf("macarrão", "macarrão", "pasta")),
        EmojiDic("🍚", listOf("arroz", "rice")),
        EmojiDic("🥬", listOf("alface", "folhas", "repolho", "couve")),
        EmojiDic("🥦", listOf("brócolis", "brocolis", "couve-flor", "couveflor", "couve flor")),
        EmojiDic("🥕", listOf("cenoura", "cenouras", "carrot", "carrots")),
        EmojiDic("🍅", listOf("tomate", "tomates", "tomato", "tomatoes")),
        EmojiDic("🥔", listOf("batata", "batatas", "potato", "potatoes")),
        EmojiDic("🥒", listOf("pepino", "pepinos", "cucumber", "cucumbers")),
        EmojiDic("🍆", listOf("berinjela", "beringela", "beringela", "eggplant")),
        EmojiDic("🌶", listOf("pimentão", "pimentao", "pimenta", "pimentas", "pepper", "peppers")),
        EmojiDic("🧅", listOf("cebola", "cebolas", "onion", "onions")),
        EmojiDic("🌽", listOf("milho", "milhos", "corn")),
        EmojiDic("🥝", listOf("kiwi", "kiwis")),
        EmojiDic("🍍", listOf("abacaxi", "abacaxis", "pineapple", "pineapples")),
        EmojiDic("🍊", listOf("laranja", "laranjas", "orange", "oranges")),
        EmojiDic("🍓", listOf("morango", "morangos", "strawberry", "strawberries")),
        EmojiDic("🍇", listOf("uva", "uvas", "grape", "grapes")),
        EmojiDic("🍈", listOf("melão", "meloes", "melon", "melons")),
        EmojiDic("🍉", listOf("melancia", "melancias", "watermelon", "watermelons")),
        EmojiDic("🍌", listOf("banana", "bananas", "banana", "bananas")),
        EmojiDic("🍋", listOf("limão", "limoes", "lemon", "lemons")),
        EmojiDic("🍐", listOf("pera", "peras", "pear", "pears")),
        EmojiDic("🍑", listOf("pêssego", "pessego", "pessegos", "pêssegos", "peach", "peaches")),
        EmojiDic("🍒", listOf("cereja", "cerejas", "cherry", "cherries")),
        EmojiDic("🍍", listOf("abacaxi", "abacaxis", "pineapple", "pineapples")),
        EmojiDic("🍎", listOf("maçã", "maca", "maçãs", "macas", "apple", "apples")),
        EmojiDic("🌻", listOf("girassol", "girassóis", "sunflower", "sunflowers")),
        EmojiDic("🥥", listOf("coco", "cocos", "coconut", "coconuts")),
        EmojiDic("🌽", listOf("milho", "milhos", "corn")),
        EmojiDic("🍄", listOf("cogumelo", "cogumelos", "mushroom", "mushrooms")),
        EmojiDic("🥜", listOf("amendoim", "amendoins", "peanut", "peanuts")),
        EmojiDic("🌰", listOf("castanha", "castanhas", "nut", "nuts")),
        EmojiDic("🍫", listOf("chocolate", "chocolates", "chocolate")),
        EmojiDic("🍯", listOf("mel", "honey", "honeys", "mels")),
        EmojiDic("🧊", listOf("gelo", "ice")),
    )

    fun emojiList() = emojisDictionary().map { it.emoji }

    data class EmojiDic(val emoji: String, val ingredientNames: List<String>)

}


