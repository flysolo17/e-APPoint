package com.flysolo.e_appoint.di

import com.flysolo.e_appoint.repository.appointment.AppointmentRepository
import com.flysolo.e_appoint.repository.appointment.AppointmentRepositoryImpl
import com.flysolo.e_appoint.repository.auth.AuthRepository
import com.flysolo.e_appoint.repository.auth.AuthRepositoryImpl
import com.flysolo.e_appoint.repository.notification.NotificationRepository
import com.flysolo.e_appoint.repository.notification.NotificationRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModules {


    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore,
        storage: FirebaseStorage,
    ): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth, firestore, storage)
    }

    @Provides
    @Singleton
    fun provideAppointmentRepository(
        firestore: FirebaseFirestore,
    ): AppointmentRepository {
        return AppointmentRepositoryImpl(firestore)
    }

    @Provides
    @Singleton
    fun provideNotificationRepository(
        firestore: FirebaseFirestore,
    ): NotificationRepository {
        return NotificationRepositoryImpl(firestore)
    }
}
