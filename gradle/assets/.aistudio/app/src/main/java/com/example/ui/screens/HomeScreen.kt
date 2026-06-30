package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Emerald
import com.example.ui.theme.Slate900
import com.example.ui.viewmodel.SharedViewModel

@Composable
fun HomeScreen(viewModel: SharedViewModel, onNavigateToAppDetails: (String) -> Unit, modifier: Modifier = Modifier) {
    val allApps by viewModel.allApps.collectAsState()
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // Hero Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(200.dp)
                .clip(androidx.compose.foundation.shape.RoundedCornerShape(32.dp))
                .background(Slate900)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_hero_banner),
                contentDescription = "Hero",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                alpha = 0.5f
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Emerald.copy(alpha = 0.4f), Color.Transparent)
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(24.dp)
            ) {
                Text("SMH Tech Apps", color = Emerald, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Professional App\nDeployment", style = MaterialTheme.typography.headlineMedium, color = Color.White, fontWeight = FontWeight.Bold, lineHeight = 32.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Premium Android solutions with clean architecture.", color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.bodySmall)
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Horizontal Scroll
        Text("Latest Apps", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(horizontal = 24.dp), fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(contentPadding = PaddingValues(horizontal = 24.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(allApps.take(3)) { app ->
                AppCardHorizontal(app, onNavigateToAppDetails)
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text("Categories", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(horizontal = 24.dp), fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        val categories = listOf("Finance", "Business", "Earning", "Education", "Productivity", "Tools", "Health")
        LazyRow(contentPadding = PaddingValues(horizontal = 24.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(categories) { cat ->
                AssistChip(
                    onClick = { },
                    label = { Text(cat) },
                    colors = AssistChipDefaults.assistChipColors(labelColor = MaterialTheme.colorScheme.onBackground)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Footer banner
        GlassCard(modifier = Modifier.padding(24.dp).fillMaxWidth()) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("SMH Tech Pvt Ltd", color = Emerald, fontWeight = FontWeight.Bold)
                Text("Professional Software Development Company", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun AppCardHorizontal(app: AppEntity, onClick: (String) -> Unit) {
    GlassCard(modifier = Modifier.width(200.dp).clickable { onClick(app.id) }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(modifier = Modifier.fillMaxWidth().height(100.dp).clip(MaterialTheme.shapes.medium).background(MaterialTheme.colorScheme.surfaceVariant)) {
               // Placeholder for app image
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(app.title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold, maxLines = 1)
            Text(app.category, style = MaterialTheme.typography.bodySmall, color = Emerald)
            Spacer(modifier = Modifier.height(8.dp))
            Text("₹${app.price}", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onBackground)
        }
    }
}
