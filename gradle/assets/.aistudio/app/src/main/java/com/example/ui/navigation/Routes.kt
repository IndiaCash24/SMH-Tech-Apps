package com.example.ui.navigation

import kotlinx.serialization.Serializable

@Serializable object SplashRoute
@Serializable object OnboardingRoute
@Serializable object LoginRoute
@Serializable object SignupRoute
@Serializable object MainRoute
@Serializable data class AppDetailsRoute(val id: String)
@Serializable data class BuyRoute(val id: String)
