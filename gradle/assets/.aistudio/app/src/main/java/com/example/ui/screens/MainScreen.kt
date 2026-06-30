package com.example.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.ui.theme.Emerald
import com.example.ui.viewmodel.SharedViewModel

@Composable
fun MainScreen(
    viewModel: SharedViewModel,
    onNavigateToAppDetails: (String) -> Unit,
    onNavigateToBuy: (String) -> Unit,
    onLogout: () -> Unit
) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Home", "Apps", "Premium", "Profile")
    val icons = listOf(Icons.Filled.Home, Icons.Filled.Apps, Icons.Filled.WorkspacePremium, Icons.Filled.Person)

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.background,
                            selectedTextColor = Emerald,
                            indicatorColor = Emerald,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Modifier.padding(innerPadding)
        when (selectedItem) {
            0 -> HomeScreen(viewModel, onNavigateToAppDetails, modifier = Modifier.padding(innerPadding))
            1 -> AppsScreen(viewModel, onNavigateToAppDetails, onNavigateToBuy, modifier = Modifier.padding(innerPadding))
            2 -> PremiumScreen(viewModel, modifier = Modifier.padding(innerPadding))
            3 -> ProfileScreen(viewModel, onLogout, modifier = Modifier.padding(innerPadding))
        }
    }
}
