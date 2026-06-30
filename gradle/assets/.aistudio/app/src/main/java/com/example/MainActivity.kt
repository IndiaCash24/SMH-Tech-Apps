package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.ui.navigation.*
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.SharedViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val appContainer = (application as SmhApplication).container
                val sharedViewModel: SharedViewModel = viewModel(
                    factory = SharedViewModel.provideFactory(
                        appContainer.authRepository,
                        appContainer.appsRepository,
                        appContainer.purchaseRepository,
                        appContainer.premiumRepository
                    )
                )

                Surface(modifier = Modifier.fillMaxSize()) {
                    SmhAppNavHost(sharedViewModel)
                }
            }
        }
    }
}

@Composable
fun SmhAppNavHost(viewModel: SharedViewModel) {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = SplashRoute) {
        composable<SplashRoute> {
            SplashScreen(onNavigateToOnboarding = {
                navController.navigate(OnboardingRoute) {
                    popUpTo(SplashRoute) { inclusive = true }
                }
            })
        }
        composable<OnboardingRoute> {
            OnboardingScreen(onFinish = {
                navController.navigate(LoginRoute) {
                    popUpTo(OnboardingRoute) { inclusive = true }
                }
            })
        }
        composable<LoginRoute> {
            LoginScreen(
                onLoginClick = { email, pass ->
                    viewModel.login(email, pass) {
                        navController.navigate(MainRoute) {
                            popUpTo(LoginRoute) { inclusive = true }
                        }
                    }
                },
                onNavigateToSignup = { navController.navigate(SignupRoute) }
            )
        }
        composable<SignupRoute> {
            SignupScreen(
                onSignupClick = { name, email, phone, pass ->
                    viewModel.register(name, email, phone, pass) {
                        navController.navigate(MainRoute) {
                            popUpTo(LoginRoute) { inclusive = true }
                        }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }
        composable<MainRoute> {
            MainScreen(
                viewModel = viewModel,
                onNavigateToAppDetails = { id -> navController.navigate(AppDetailsRoute(id)) },
                onNavigateToBuy = { id -> navController.navigate(BuyRoute(id)) },
                onLogout = {
                    navController.navigate(LoginRoute) {
                        popUpTo(MainRoute) { inclusive = true }
                    }
                }
            )
        }
        composable<AppDetailsRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<AppDetailsRoute>()
            AppDetailsScreen(
                appId = route.id,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onBuy = { id -> navController.navigate(BuyRoute(id)) }
            )
        }
        composable<BuyRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<BuyRoute>()
            BuyScreen(
                appId = route.id,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSuccess = {
                    navController.navigate(MainRoute) {
                        popUpTo(MainRoute) { inclusive = true }
                    }
                }
            )
        }
    }
}
