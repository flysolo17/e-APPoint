package com.flysolo.e_appoint.models.users



data class Users(
    var id : String ? = null,
    val name : String? = null,
    val phone : String ? = null,
    val email : String ? = null,
    val profile : String ? = null,
    val type : UserType = UserType.CLIENT

)


enum class UserType {
    ADMIN,
    CLIENT
}