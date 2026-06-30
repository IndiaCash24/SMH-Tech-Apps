package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.AppEntity
import com.example.data.local.entity.UserEntity
import com.example.domain.repository.AppsRepository
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.PremiumRepository
import com.example.domain.repository.PurchaseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SharedViewModel(
    private val authRepository: AuthRepository,
    private val appsRepository: AppsRepository,
    private val purchaseRepository: PurchaseRepository,
    private val premiumRepository: PremiumRepository
) : ViewModel() {

    val currentUser: StateFlow<UserEntity?> = authRepository.getCurrentUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allApps: StateFlow<List<AppEntity>> = appsRepository.getAllApps()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val purchasedApps: StateFlow<List<AppEntity>> = purchaseRepository.getPurchasedApps()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            appsRepository.seedDemoApps()
        }
    }

    fun login(email: String, pass: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val success = authRepository.login(email, pass)
            if(success) onSuccess()
        }
    }
    
    fun register(fullName: String, email: String, phone: String, pass: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val user = UserEntity(fullName = fullName, email = email, phone = phone)
            authRepository.register(user)
            onSuccess()
        }
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout()
            onSuccess()
        }
    }

    fun upgradePremium(plan: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            premiumRepository.upgradeToPremium(plan)
            onSuccess()
        }
    }

    fun purchaseApp(appId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            purchaseRepository.purchaseApp(appId)
            onSuccess()
        }
    }

    fun toggleWishlist(appId: String, inWishlist: Boolean) {
        viewModelScope.launch {
            appsRepository.toggleWishlist(appId, inWishlist)
        }
    }

    fun updateCurrentUser(user: UserEntity) {
        viewModelScope.launch {
            authRepository.updateUser(user)
        }
    }

    fun updateApp(app: AppEntity) {
        viewModelScope.launch {
            appsRepository.updateApp(app)
        }
    }

    fun resetAllApps() {
        viewModelScope.launch {
            appsRepository.resetAllApps()
        }
    }

    companion object {
        fun provideFactory(
            authRepository: AuthRepository,
            appsRepository: AppsRepository,
            purchaseRepository: PurchaseRepository,
            premiumRepository: PremiumRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SharedViewModel(authRepository, appsRepository, purchaseRepository, premiumRepository) as T
            }
        }
    }
}
