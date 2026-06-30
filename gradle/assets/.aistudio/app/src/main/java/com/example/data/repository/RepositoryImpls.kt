package com.example.data.repository

import com.example.data.local.dao.AppDao
import com.example.data.local.dao.UserDao
import com.example.data.local.entity.AppEntity
import com.example.data.local.entity.UserEntity
import com.example.domain.repository.AppsRepository
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.PremiumRepository
import com.example.domain.repository.PurchaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl(private val userDao: UserDao) : AuthRepository {
    override fun getCurrentUser(): Flow<UserEntity?> = userDao.getUser()

    override suspend fun login(email: String, pass: String): Boolean {
        // Mock authentication
        val user = userDao.getUser().firstOrNull()
        if (user == null) {
            userDao.insertUser(UserEntity(fullName = "Demo User", email = email, phone = "0000"))
        }
        return true
    }

    override suspend fun register(user: UserEntity): Boolean {
        userDao.insertUser(user)
        return true
    }

    override suspend fun logout() {
        userDao.clearUser()
    }
}

class AppsRepositoryImpl(private val appDao: AppDao) : AppsRepository {
    override fun getAllApps(): Flow<List<AppEntity>> = appDao.getAllApps()

    override fun getAppDetails(id: String): Flow<AppEntity?> = appDao.getAppById(id)

    override suspend fun seedDemoApps() {
        val existing = appDao.getAllApps().firstOrNull()
        if (existing.isNullOrEmpty()) {
            val demoApps = listOf(
                AppEntity("1", "E-Commerce Pro", "Full flutter e-commerce UI with admin panel", "Business", 4.8f, "1k+", 999.0),
                AppEntity("2", "AI Chatbot UI", "Modern AI chatbot interface with Gemini", "Tools", 4.9f, "500+", 499.0),
                AppEntity("3", "Fitness Tracker", "Complete fitness tracking application", "Health", 4.7f, "2k+", 1499.0),
                AppEntity("4", "Crypto Wallet", "Secure crypto wallet UI and charts", "Finance", 4.6f, "3k+", 999.0),
                AppEntity("5", "Social Media", "Feature-rich social media layout", "Entertainment", 4.5f, "10k+", 1499.0)
            )
            appDao.insertApps(demoApps)
        }
    }

    override suspend fun toggleWishlist(appId: String, inWishlist: Boolean) {
        val app = appDao.getAppById(appId).firstOrNull()
        if (app != null) {
            appDao.updateApp(app.copy(inWishlist = inWishlist))
        }
    }
}

class PurchaseRepositoryImpl(private val appDao: AppDao, private val userDao: UserDao) : PurchaseRepository {
    override suspend fun purchaseApp(appId: String): Boolean {
        val app = appDao.getAppById(appId).firstOrNull()
        if (app != null) {
            appDao.updateApp(app.copy(isPurchased = true))
            return true
        }
        return false
    }

    override fun getPurchasedApps(): Flow<List<AppEntity>> {
        return appDao.getAllApps().map { apps -> apps.filter { it.isPurchased } }
    }
}

class PremiumRepositoryImpl(private val userDao: UserDao) : PremiumRepository {
    override suspend fun upgradeToPremium(plan: String): Boolean {
        val user = userDao.getUser().firstOrNull()
        if (user != null) {
            userDao.updateUser(user.copy(premiumStatus = true))
            return true
        }
        return false
    }
}
