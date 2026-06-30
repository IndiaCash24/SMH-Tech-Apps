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

    override suspend fun updateUser(user: UserEntity) {
        userDao.updateUser(user)
    }
}

class AppsRepositoryImpl(private val appDao: AppDao) : AppsRepository {
    override fun getAllApps(): Flow<List<AppEntity>> = appDao.getAllApps()

    override fun getAppDetails(id: String): Flow<AppEntity?> = appDao.getAppById(id)

    override suspend fun seedDemoApps() {
        val existing = appDao.getAllApps().firstOrNull()
        if (existing.isNullOrEmpty()) {
            val demoApps = listOf(
                // 10 Regular Apps
                AppEntity(id = "1", title = "E-Commerce Pro", description = "Full flutter e-commerce UI with admin panel", category = "Business", rating = 4.8f, downloads = "1.2k+", price = 999.0, isPremium = false),
                AppEntity(id = "2", title = "AI Chatbot UI", description = "Modern AI chatbot interface with Gemini", category = "Tools", rating = 4.9f, downloads = "800+", price = 499.0, isPremium = false),
                AppEntity(id = "3", title = "Fitness Tracker", description = "Complete fitness tracking application with charts", category = "Health", rating = 4.7f, downloads = "2.5k+", price = 1499.0, isPremium = false),
                AppEntity(id = "4", title = "Crypto Wallet", description = "Secure crypto wallet UI and realtime charts", category = "Finance", rating = 4.6f, downloads = "3.1k+", price = 999.0, isPremium = false),
                AppEntity(id = "5", title = "Social Media App", description = "Feature-rich social media layout with feeds", category = "Entertainment", rating = 4.5f, downloads = "12k+", price = 1499.0, isPremium = false),
                AppEntity(id = "6", title = "Grocery Delivery", description = "Fast local grocery ordering and delivery app", category = "Business", rating = 4.7f, downloads = "950+", price = 1299.0, isPremium = false),
                AppEntity(id = "7", title = "Task Manager", description = "Productivity board with tasks and reminders", category = "Productivity", rating = 4.4f, downloads = "5.4k+", price = 399.0, isPremium = false),
                AppEntity(id = "8", title = "Music Player", description = "Sleek music player with playlists and local playback", category = "Entertainment", rating = 4.8f, downloads = "15k+", price = 899.0, isPremium = false),
                AppEntity(id = "9", title = "Recipe Book UI", description = "Discover and save your favorite delicious food recipes", category = "Lifestyle", rating = 4.3f, downloads = "1.8k+", price = 299.0, isPremium = false),
                AppEntity(id = "10", title = "E-Learning Hub", description = "Access online lectures and courses with quizzes", category = "Education", rating = 4.6f, downloads = "4.2k+", price = 1599.0, isPremium = false),

                // 10 Premium Apps (Fixed at 4999.0)
                AppEntity(id = "11", title = "Enterprise ERP", description = "Complete enterprise resource planning solution with custom modules", category = "Premium", rating = 4.9f, downloads = "500+", price = 4999.0, isPremium = true),
                AppEntity(id = "12", title = "AI Video Editor Pro", description = "Automated high-quality AI video rendering and filters", category = "Premium", rating = 5.0f, downloads = "1.1k+", price = 4999.0, isPremium = true),
                AppEntity(id = "13", title = "Advanced Cloud CRM", description = "Secure client relationship manager with cloud sync and pipelines", category = "Premium", rating = 4.8f, downloads = "350+", price = 4999.0, isPremium = true),
                AppEntity(id = "14", title = "Smart IoT Home Hub", description = "Universal hardware dashboard supporting diverse home appliances", category = "Premium", rating = 4.9f, downloads = "720+", price = 4999.0, isPremium = true),
                AppEntity(id = "15", title = "DeFi Trading Terminal", description = "Real-time analytics and decentralized exchange wallet integrations", category = "Premium", rating = 4.8f, downloads = "900+", price = 4999.0, isPremium = true),
                AppEntity(id = "16", title = "Telemedicine Platform", description = "Secure, end-to-end video consultations and electronic prescriptions", category = "Premium", rating = 4.7f, downloads = "450+", price = 4999.0, isPremium = true),
                AppEntity(id = "17", title = "AI Code Copilot", description = "Self-improving AI code helper for local development environments", category = "Premium", rating = 4.9f, downloads = "1.5k+", price = 4999.0, isPremium = true),
                AppEntity(id = "18", title = "VR Property Viewer", description = "Immersive virtual reality 3D tours for premium real estate", category = "Premium", rating = 4.9f, downloads = "280+", price = 4999.0, isPremium = true),
                AppEntity(id = "19", title = "Supply Chain SaaS", description = "Realtime shipment tracking and inventory planning optimization", category = "Premium", rating = 4.8f, downloads = "640+", price = 4999.0, isPremium = true),
                AppEntity(id = "20", title = "Fleet Manager Pro", description = "Enterprise GPS tracking and fuel efficiency reporting software", category = "Premium", rating = 4.9f, downloads = "810+", price = 4999.0, isPremium = true)
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

    override suspend fun updateApp(app: AppEntity) {
        appDao.updateApp(app)
    }

    override suspend fun resetAllApps() {
        val apps = appDao.getAllApps().firstOrNull() ?: emptyList()
        apps.forEach { app ->
            appDao.updateApp(app.copy(isPurchased = false, inWishlist = false))
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
