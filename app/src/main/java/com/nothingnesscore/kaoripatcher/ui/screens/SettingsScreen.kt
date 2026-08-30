package com.nothingnesscore.kaoripatcher.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                var autoPatch by remember { mutableStateOf(false) }
                var darkMode by remember { mutableStateOf(true) }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Auto-Patch on Startup")
                    Switch(checked = autoPatch, onCheckedChange = { autoPatch = it })
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Force Dark Mode")
                    Switch(checked = darkMode, onCheckedChange = { darkMode = it })
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = { /* Clear cache logic */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Clear App Cache")
        }
    }
}
