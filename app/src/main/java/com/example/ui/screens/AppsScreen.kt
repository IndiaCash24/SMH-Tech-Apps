package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.local.entity.AppEntity
import com.example.ui.components.GlassCard
import com.example.ui.components.hapticClickable
import com.example.ui.components.rememberHapticFeedback
import com.example.ui.theme.Emerald
import com.example.ui.viewmodel.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppsScreen(viewModel: SharedViewModel, onNavigateToAppDetails: (String) -> Unit, onNavigateToBuy: (String) -> Unit, modifier: Modifier = Modifier) {
    val allApps by viewModel.allApps.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    
    Column(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // Top bar
        TopAppBar(
            title = { Text("All Apps", fontWeight = FontWeight.Bold) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background, titleContentColor = MaterialTheme.colorScheme.onBackground),
            windowInsets = WindowInsets(0, 0, 0, 0)
        )
        
        // Search
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),
            placeholder = { Text("Search Apps...") },
            shape = MaterialTheme.shapes.extraLarge,
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Emerald)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            val filtered = allApps.filter { !it.isPremium && it.title.contains(searchQuery, ignoreCase = true) }
            items(filtered) { app ->
                AppCardGrid(app, onNavigateToAppDetails, onNavigateToBuy)
            }
        }
    }
}

@Composable
fun AppCardGrid(app: AppEntity, onClick: (String) -> Unit, onBuyClick: (String) -> Unit, onToggleWishlist: () -> Unit = {}) {
    val haptic = rememberHapticFeedback()
    GlassCard(modifier = Modifier.fillMaxWidth().hapticClickable { onClick(app.id) }) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(modifier = Modifier.fillMaxWidth().height(100.dp).clip(MaterialTheme.shapes.medium).background(MaterialTheme.colorScheme.surfaceVariant)) {
                Image(
                    painter = painterResource(id = R.drawable.img_onboarding), // Using a random image for now
                    contentDescription = "App Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                IconButton(
                    onClick = {
                        haptic()
                        onToggleWishlist()
                    },
                    modifier = Modifier.align(Alignment.TopEnd).padding(4.dp).size(32.dp)
                ) {
                    Icon(
                        imageVector = if (app.inWishlist) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (app.inWishlist) Emerald else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(app.title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold, maxLines = 1)
            Text(app.category, style = MaterialTheme.typography.bodySmall, color = Emerald)
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("₹${app.price}", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onBackground)
                Button(
                    onClick = { 
                        haptic()
                        onBuyClick(app.id) 
                    },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                    modifier = Modifier.height(32.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Emerald)
                ) {
                    Text("Buy", fontSize = MaterialTheme.typography.bodySmall.fontSize, color = MaterialTheme.colorScheme.background)
                }
            }
        }
    }
}
