package com.ilustris.cuccina.ui.theme

import com.ilustris.cuccina.feature.recipe.ingredient.domain.model.Ingredient
import com.ilustris.cuccina.feature.recipe.step.domain.model.Step

sealed class FormPage(
    title: String,
    description: String,
    val actionText: String,
    val sendData: (Any) -> Unit
) : Page(title, description) {


    class ImageFormPage(sendData: (String) -> Unit) :
        FormPage(
            "Foto da receita",
            "Envie uma foto da receita", "Enviar foto", {
                sendData(it as String)
            }
        )

    class NameFormPage(sendData: (String) -> Unit) : FormPage(
        "Nome da receita",
        "Digite o nome da receita", "Continuar", {
            sendData(it as String)
        }
    )

    class DescriptionFormPage(sendData: (String) -> Unit) : FormPage(
        "Descrição da receita",
        "Digite a descrição da receita", "Continuar", { sendData(it as String) }
    )

    class TimeFormPage(sendData: (Long) -> Unit) : FormPage(
        "Tempo de preparo",
        "Digite o tempo de preparo da receita", "Continuar", { sendData(it as Long) }
    )

    class PortionsFormPage(sendData: (Int) -> Unit) : FormPage(
        "Porções",
        "Digite o número de porções da receita", "Continuar", { sendData(it as Int) }
    )

    class CategoryFormPage(sendData: (String) -> Unit) : FormPage(
        "Categoria",
        "Escolha uma categoria para sua receita", "Continuar", { sendData(it as String) }
    )

    class CaloriesFormPage(sendData: (Int) -> Unit) : FormPage(
        "Calorias",
        "Digite a quantidade de calorias da receita", "Continuar", { sendData(it as Int) }
    )

    class IngredientsFormPage(sendData: (List<Ingredient>) -> Unit) : FormPage(
        "Ingredientes",
        "Digite os ingredientes da receita", "Continuar", { sendData(it as List<Ingredient>) }
    )

    class StepsFormPage(val currentIngredients: List<Ingredient>, sendData: (List<Step>) -> Unit) :
        FormPage(
            "Passos",
            "Digite os passos da receita", "Salvar", { sendData(it as List<Step>) }
        )

}