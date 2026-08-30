package com.nothingnesscore.kaoripatcher.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    isLiquidGlassEnabled: Boolean,
    onLiquidGlassToggle: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(bottom = 80.dp) // Leave space for nav bar
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Column {
                        Text("Liquid Glass Navigation", style = MaterialTheme.typography.titleMedium)
                        Text("Enable HyperOS glassmorphism effects", style = MaterialTheme.typography.bodyMedium)
                    }
                    Switch(
                        checked = isLiquidGlassEnabled,
                        onCheckedChange = onLiquidGlassToggle
                    )
                }
            }
        }
    }
}
