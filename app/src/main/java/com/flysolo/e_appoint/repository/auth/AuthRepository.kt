package com.flysolo.e_appoint.repository.auth

import android.net.Uri
import com.flysolo.e_appoint.models.users.UserWithVerification
import com.flysolo.e_appoint.models.users.Users
import com.flysolo.e_appoint.utils.UiState


interface AuthRepository {

    suspend fun signInWithEmailAndPassword(
        email : String,
        password : String,
    ) : Result<UserWithVerification?>
    suspend fun saveUser(user: Users)

    suspend fun getCurrentUser() : Result<UserWithVerification?>
    suspend fun register(
        user: Users,
        password: String,

        ) : Result<String>

    suspend fun logout()




    suspend fun sendEmailVerification() : Result<String>

    suspend fun listenToUserEmailVerification() : Result<Boolean>

    suspend fun forgotPassword(email : String) : Result<String>




    suspend fun getUser(id : String) : Result<Users?>

    suspend fun changePassword(oldPassword : String,newPassword : String,result: (UiState<String>) -> Unit)



    suspend fun updateUserInfo(
        uid : String,
        name : String,
        phone : String,
        result: (UiState<String>) -> Unit
    )
    suspend fun deleteAccount(
        uid : String,
        password : String,
        result: (UiState<String>) -> Unit
    )

    suspend fun changeProfile(
        uid : String,
        uri: Uri,
        result: (UiState<String>) -> Unit
    )


}