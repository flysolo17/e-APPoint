package com.flysolo.e_appoint.repository.auth

import android.net.Uri
import android.util.Log
import com.flysolo.e_appoint.models.users.UserWithVerification
import com.flysolo.e_appoint.models.users.Users
import com.flysolo.e_appoint.utils.UiState
import com.flysolo.e_appoint.utils.generateRandomString
import com.google.firebase.FirebaseException
import com.google.firebase.auth.EmailAuthProvider

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
const val USERS_COLLECTION = "users"
const val APPOINTMENT_COLLECTION = "appointments"

class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    ): AuthRepository {

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Result<UserWithVerification?> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
            if (firebaseUser == null) {
                Result.success(null)
            } else {
                val userDocument = firestore.collection(USERS_COLLECTION)
                    .document(firebaseUser.uid)
                    .get()
                    .await()
                if (userDocument.exists()) {
                    val user = userDocument.toObject(Users::class.java)
                    val userWithVerification = UserWithVerification(
                        users = user!!,
                        isVerified = firebaseUser.isEmailVerified
                    )
                    Result.success(userWithVerification)
                } else {

                    Result.success(null)
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun saveUser(user: Users) {
        try {
            val userRef = firestore.collection(USERS_COLLECTION).document(user.id!!)
            val documentSnapshot = userRef.get().await()

            if (!documentSnapshot.exists()) {
                userRef.set(user).await()
            }
        } catch (e: Exception) {
            throw Exception("Failed to save user: ${e.localizedMessage}")
        }
    }


    override suspend fun getCurrentUser(): Result<UserWithVerification?> {
        return try {
            val uid = auth.currentUser?.uid
            if (uid != null) {
                val userRef = firestore.collection(USERS_COLLECTION).document(uid)
                val documentSnapshot = userRef.get().await()
                val user = documentSnapshot.toObject(Users::class.java)
                delay(1000)
                val userWithVerification = UserWithVerification(
                    users= user!!,
                    isVerified = auth.currentUser?.isEmailVerified ?: false
                )
                Result.success(userWithVerification)
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {

            Result.failure(e)
        }
    }


    override suspend fun register(user: Users, password: String): Result<String> {
        return try {
            val firestoreBatch = firestore.batch()
            val authResult = auth.createUserWithEmailAndPassword(user.email!!, password).await()
            val userId = authResult.user?.uid ?: throw IllegalStateException("User ID not found after registration")
            user.id = userId
            val userRef = firestore.collection(USERS_COLLECTION).document(userId)
            firestoreBatch.set(userRef, user)

            firestoreBatch.commit().await()
            Result.success("Successfully Created!")
        } catch (e: Exception) {
            Log.d("register",e.message,e)
            Result.failure(e)
        }
    }



    override suspend fun logout() {
        auth.signOut()
    }

    override suspend fun sendEmailVerification(): Result<String> {
        return try {
            val currentUser = auth.currentUser

            if (currentUser != null) {
                if (currentUser.isEmailVerified) {
                    Result.success("User Verified.")
                } else {
                    currentUser.sendEmailVerification().await()
                    Result.success("Verification email sent successfully.")
                }

            } else {
                Result.failure(Exception("No authenticated user found."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun listenToUserEmailVerification(): Result<Boolean> {
        return try {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                // Reload the user to get the latest email verification status
                currentUser.reload().await()
                val isVerified = currentUser.isEmailVerified
                Result.success(isVerified)
            } else {
                Result.failure(Exception("No authenticated user found."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun forgotPassword(email: String): Result<String> {
        return try {

            if (email.isEmpty()) {
                return Result.failure(Exception("Email cannot be empty."))
            }


            auth.sendPasswordResetEmail(email).await()


            Result.success("Password reset email sent successfully.")
        } catch (e: FirebaseAuthInvalidUserException) {

            Result.failure(Exception("No account found with this email address."))
        } catch (e: Exception) {

            Result.failure(Exception("Failed to send password reset email: ${e.message}"))
        }
    }

    override suspend fun getUser(id: String): Result<Users?> {
        return try {
            val result = firestore.collection(USERS_COLLECTION).document(id).get().await().toObject<Users>()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun changePassword(
        oldPassword: String,
        newPassword: String,
        result: (UiState<String>) -> Unit
    ) {
        try {
            result.invoke(UiState.Loading)
            val currentUser = auth.currentUser

            if (currentUser != null) {
                val credential = EmailAuthProvider.getCredential(currentUser.email!!, oldPassword)
                currentUser.reauthenticate(credential).await()
                currentUser.updatePassword(newPassword).await()

                result.invoke(UiState.Success("Password updated successfully."))
            } else {

                result.invoke(UiState.Error("User is not logged in."))
            }
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            result.invoke(UiState.Error("Old password is incorrect."))
        } catch (e: Exception) {
            result.invoke(UiState.Error(e.message.toString()))
        }
    }

    override suspend fun updateUserInfo(
        uid: String,
        name: String,
        phone: String,
        result: (UiState<String>) -> Unit
    ) {
        try {
            // Assume you have a Firestore instance to update user info in a "users" collection
            val userDocRef = firestore.collection(USERS_COLLECTION).document(uid)

            // Update the user document with the new info
            userDocRef.update(
                mapOf(
                    "name" to name,
                    "phone" to phone
                )
            ).await()

            result(UiState.Success("User information updated successfully!"))
        } catch (e: Exception) {
            result(UiState.Error(e.localizedMessage ?: "Failed to update user info"))
        }
    }
    override suspend fun deleteAccount(uid: String,password: String, result: (UiState<String>) -> Unit) {
        try {
            result(UiState.Loading)

            val user = auth.currentUser
            if (user != null) {
                val email = user.email
                val credential = EmailAuthProvider.getCredential(email!!, password)

                user.reauthenticate(credential).await()
                user.delete().await()
            }

            val userDocRef = firestore.collection(USERS_COLLECTION).document(uid)
            userDocRef.delete().await()

            // Delete all appointments associated with the user
            val appointmentQuery = firestore.collection(APPOINTMENT_COLLECTION)
                .whereEqualTo("userID", uid)
                .get()
                .await()

            for (document in appointmentQuery.documents) {
                document.reference.delete().await()
            }




            result(UiState.Success("Account deleted successfully"))
        } catch (e: Exception) {
            result(UiState.Error(e.localizedMessage ?: "Failed to delete account"))
        }
    }

    override suspend fun changeProfile(uid: String, uri: Uri, result: (UiState<String>) -> Unit) {
        try {
            result(UiState.Loading)


            val storageReference = storage.reference
            val profilePictureRef = storageReference.child("profile_pictures/${generateRandomString(6)}.jpg")

            val uploadTask = profilePictureRef.putFile(uri).await()


            val downloadUrl = profilePictureRef.downloadUrl.await()

            firestore.collection(USERS_COLLECTION)
                .document(uid)
                .update("profile",downloadUrl)
                .await()
            result(UiState.Success("Profile picture updated: $downloadUrl"))

        } catch (exception: Exception) {
            // Handle errors and return error state
            result(UiState.Error("Error: ${exception.message}"))
        } catch (e : FirebaseException) {
            // Handle errors and return error state
            result(UiState.Error("Error: ${e.message}"))
        }
    }


}