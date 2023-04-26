package com.ilustris.cuccina.feature.recipe.domain.service

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.ilustris.cuccina.feature.recipe.domain.model.Recipe
import com.silent.ilustriscore.core.model.BaseService

class RecipeService : BaseService() {
    override val dataPath: String = "recipes"

    override fun deserializeDataSnapshot(dataSnapshot: DocumentSnapshot): Recipe? {
        dataSnapshot.toObject(Recipe::class.java)?.let {
            it.id = dataSnapshot.id
            return it
        }
        return null
    }

    override fun deserializeDataSnapshot(dataSnapshot: QueryDocumentSnapshot): Recipe {
        return dataSnapshot.toObject(Recipe::class.java)
    }
}