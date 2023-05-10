package com.ilustris.cuccina.feature.profile.domain.service

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.ilustris.cuccina.feature.profile.domain.model.UserModel
import com.silent.ilustriscore.core.bean.BaseBean
import com.silent.ilustriscore.core.model.BaseService
import com.silent.ilustriscore.core.model.DataException
import com.silent.ilustriscore.core.model.ServiceResult

class UserService : BaseService() {
    override val dataPath: String = "users"

    override fun deserializeDataSnapshot(dataSnapshot: DocumentSnapshot): UserModel? =
        dataSnapshot.let {
            it.toObject(UserModel::class.java)?.apply {
                id = it.id
            }
        }

    override fun deserializeDataSnapshot(dataSnapshot: QueryDocumentSnapshot): BaseBean =
        dataSnapshot.toObject(UserModel::class.java).apply {
            id = dataSnapshot.id
        }

    suspend fun saveUser(): ServiceResult<DataException, BaseBean> {
        val user = currentUser()
        return editData(
            UserModel(
                id = user?.uid ?: "",
                name = user?.displayName ?: "",
                uid = user?.uid ?: "",
                photoUrl = user?.photoUrl.toString()
            )
        )
    }
}