package com.ilustris.cuccina.feature.recipe.domain.service

import android.net.Uri
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.storage.FirebaseStorage
import com.ilustris.cuccina.feature.recipe.domain.model.Recipe
import com.silent.ilustriscore.core.bean.BaseBean
import com.silent.ilustriscore.core.model.BaseService
import com.silent.ilustriscore.core.model.DataException
import com.silent.ilustriscore.core.model.ServiceResult
import kotlinx.coroutines.tasks.await

class RecipeService : BaseService() {
    override val dataPath: String = "recipes"

    override var requireAuth: Boolean = true

    override fun deserializeDataSnapshot(dataSnapshot: DocumentSnapshot): Recipe? {
        dataSnapshot.toObject(Recipe::class.java)?.let {
            it.id = dataSnapshot.id
            return it
        }
        return null
    }

    private fun storageReference() = FirebaseStorage.getInstance().reference.child(dataPath)


    override suspend fun addData(data: BaseBean): ServiceResult<DataException, BaseBean> {
        try {
            val user = currentUser()!!
            val recipe = data as Recipe
            val uploadTask =
                storageReference().storage.reference.child(recipe.name.lowercase().trim())
                    .putFile(Uri.parse(recipe.photo)).await()
            return if (uploadTask.task.isSuccessful) {
                recipe.photo = uploadTask.storage.downloadUrl.await().toString()
                recipe.userID = user.uid
                super.addData(data)
            } else {
                ServiceResult.Error(DataException.UPLOAD)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return ServiceResult.Error(DataException.SAVE)
        }
    }

    override suspend fun deleteData(id: String): ServiceResult<DataException, Boolean> {
        return try {
            val recipe = getSingleData(id).success.data as Recipe
            storageReference().storage.reference.child(recipe.name.lowercase().trim()).delete()
                .await()
            super.deleteData(recipe.id)
        } catch (e: Exception) {
            e.printStackTrace()
            ServiceResult.Error(DataException.DELETE)
        }
    }

    override fun deserializeDataSnapshot(dataSnapshot: QueryDocumentSnapshot): Recipe {
        return dataSnapshot.toObject(Recipe::class.java)
    }

    suspend fun getRecipesByUserLike(userID: String): ServiceResult<DataException, ArrayList<BaseBean>> {
        if (requireAuth && currentUser() == null) return ServiceResult.Error(DataException.AUTH)
        val query = reference.whereArrayContains("likes", userID).get().await()
        return if (!query.isEmpty) {
            ServiceResult.Success(getDataList(query.documents))
        } else {
            ServiceResult.Error(DataException.NOTFOUND)
        }
    }

    suspend fun getRecipesByUser(userID: String) = query(userID, "userID")
    suspend fun getRecipesByCategory(category: String) = query(category, "category")
}