package com.ilustris.cuccina.feature.profile.domain.model

import com.silent.ilustriscore.core.bean.BaseBean

data class UserModel(
    override var id: String = "",
    var name: String = "",
    var uid: String = "",
    var photoUrl: String = ""
) : BaseBean(id)
