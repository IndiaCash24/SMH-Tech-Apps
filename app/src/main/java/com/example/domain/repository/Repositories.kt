package com.example.domain.repository

import com.example.data.local.entity.AppEntity
import com.example.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getCurrentUser(): Flow<UserEntity?>
    suspend fun login(email: String, pass: String): Boolean
    suspend fun register(user: UserEntity): Boolean
    suspend fun logout()
    suspend fun updateUser(user: UserEntity)
}

interface AppsRepository {
    fun getAllApps(): Flow<List<AppEntity>>
    fun getAppDetails(id: String): Flow<AppEntity?>
    suspend fun seedDemoApps() // To pre-populate Room
    suspend fun toggleWishlist(appId: String, inWishlist: Boolean)
    suspend fun updateApp(app: AppEntity)
    suspend fun resetAllApps()
}

interface PurchaseRepository {
    suspend fun purchaseApp(appId: String): Boolean
    fun getPurchasedApps(): Flow<List<AppEntity>>
}

interface PremiumRepository {
    suspend fun upgradeToPremium(plan: String): Boolean
}

interface NotificationRepository {
    // Demo repository
}
