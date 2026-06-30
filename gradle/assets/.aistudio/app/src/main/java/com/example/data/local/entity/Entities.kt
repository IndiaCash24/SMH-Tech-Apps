package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fullName: String,
    val email: String,
    val phone: String,
    val premiumStatus: Boolean = false,
    val walletBalance: Double = 0.0,
    val isDarkMode: Boolean = true
)

@Serializable
@Entity(tableName = "apps")
data class AppEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val category: String,
    val rating: Float,
    val downloads: String,
    val price: Double,
    val isPurchased: Boolean = false,
    val inWishlist: Boolean = false
)
