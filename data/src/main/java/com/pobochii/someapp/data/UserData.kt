package com.pobochii.someapp.data

import com.google.gson.annotations.SerializedName
import com.pobochii.someapp.domain.users.User

/**
 *
 */
data class UserData(
    @SerializedName("name") val name: String = "",
    @SerializedName("image") val image: String = "",
)

/**
 * [UserData] to domain [User] mapper
 */
fun UserData.asUser(id: Int) = User(id, name, image)
