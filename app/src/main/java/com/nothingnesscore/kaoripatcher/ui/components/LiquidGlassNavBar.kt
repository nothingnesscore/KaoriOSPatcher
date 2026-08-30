package com.nothingnesscore.kaoripatcher.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.nothingnesscore.kaoripatcher.ui.theme.BlueAccent

@Composable
fun LiquidGlassNavBar(
    selectedTab: Int,
    isLiquidGlassEnabled: Boolean,
    onTabSelected: (Int) -> Unit
) {
    val isDark = isSystemInDarkTheme()
    
    // Fallback standard colors if liquid glass is disabled
    val fallbackColor = if (isDark) Color(0xFF1E1E1E) else Color(0xFFF5F5F5)

    // Advanced glassmorphism gradient
    val glassGradient = Brush.verticalGradient(
        colors = if (isDark) listOf(
            Color(0x55000000), 
            Color(0x33000000)  
        ) else listOf(
            Color(0x99FFFFFF),
            Color(0x66FFFFFF)
        )
    )

    val borderGradient = Brush.linearGradient(
        colors = if (isDark) listOf(
            Color(0x44FFFFFF),
            Color(0x11FFFFFF)
        ) else listOf(
            Color(0x88FFFFFF),
            Color(0x22FFFFFF)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp, start = 32.dp, end = 32.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(32.dp))
                .then(
                    if (isLiquidGlassEnabled) {
                        Modifier
                            .background(glassGradient)
                            .border(
                                width = 1.dp,
                                brush = borderGradient,
                                shape = RoundedCornerShape(32.dp)
                            )
                            .graphicsLayer {
                                shadowElevation = 16f
                                shape = RoundedCornerShape(32.dp)
                                clip = true
                            }
                    } else {
                        Modifier.background(fallbackColor)
                    }
                )
                .padding(horizontal = 24.dp, vertical = 14.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavBarItem("Home", 0, selectedTab, onTabSelected)
            NavBarItem("Patcher", 1, selectedTab, onTabSelected)
            NavBarItem("Settings", 2, selectedTab, onTabSelected)
        }
    }
}

@Composable
fun NavBarItem(
    title: String,
    index: Int,
    selectedIndex: Int,
    onClick: (Int) -> Unit
) {
    val isSelected = index == selectedIndex
    val color = if (isSelected) BlueAccent else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    
    val scale by animateFloatAsState(targetValue = if (isSelected) 1.1f else 1.0f)
    
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { onClick(index) }
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = color,
            style = MaterialTheme.typography.labelLarge
        )
    }
}
