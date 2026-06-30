package com.example.di

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.repository.AppsRepositoryImpl
import com.example.data.repository.AuthRepositoryImpl
import com.example.data.repository.PremiumRepositoryImpl
import com.example.data.repository.PurchaseRepositoryImpl
import com.example.domain.repository.AppsRepository
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.PremiumRepository
import com.example.domain.repository.PurchaseRepository

interface AppContainer {
    val authRepository: AuthRepository
    val appsRepository: AppsRepository
    val purchaseRepository: PurchaseRepository
    val premiumRepository: PremiumRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    private val database: AppDatabase by lazy { AppDatabase.getDatabase(context) }

    override val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(database.userDao())
    }

    override val appsRepository: AppsRepository by lazy {
        AppsRepositoryImpl(database.appDao())
    }

    override val purchaseRepository: PurchaseRepository by lazy {
        PurchaseRepositoryImpl(database.appDao(), database.userDao())
    }

    override val premiumRepository: PremiumRepository by lazy {
        PremiumRepositoryImpl(database.userDao())
    }
}
