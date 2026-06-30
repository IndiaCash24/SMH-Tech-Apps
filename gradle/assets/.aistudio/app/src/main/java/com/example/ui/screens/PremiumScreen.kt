package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.components.GlassCard
import com.example.ui.theme.Emerald
import com.example.ui.viewmodel.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumScreen(viewModel: SharedViewModel, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        TopAppBar(
            title = { Text("Premium Membership", fontWeight = FontWeight.Bold) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background, titleContentColor = MaterialTheme.colorScheme.onBackground)
        )
        
        Column(modifier = Modifier.padding(24.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Unlock Everything", style = MaterialTheme.typography.headlineMedium, color = Emerald, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Get unlimited source code downloads, priority support, and early access.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            
            Spacer(modifier = Modifier.height(32.dp))
            
            PremiumPlanCard("Monthly", "₹499", "Billed monthly", viewModel)
            Spacer(modifier = Modifier.height(16.dp))
            PremiumPlanCard("Quarterly", "₹999", "Billed every 3 months", viewModel)
            Spacer(modifier = Modifier.height(16.dp))
            PremiumPlanCard("Yearly", "₹2999", "Best value! Billed yearly", viewModel)
        }
    }
}

@Composable
fun PremiumPlanCard(title: String, price: String, desc: String, viewModel: SharedViewModel) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(24.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(title, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
                Text(desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(8.dp))
                Text(price, style = MaterialTheme.typography.headlineSmall, color = Emerald, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = { viewModel.upgradePremium(title) {} },
                colors = ButtonDefaults.buttonColors(containerColor = Emerald)
            ) {
                Text("Select", color = MaterialTheme.colorScheme.background, fontWeight = FontWeight.Bold)
            }
        }
    }
}
