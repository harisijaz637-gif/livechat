package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.FloatingHeart

@Composable
fun FloatingHeartsLayer(
    hearts: List<FloatingHeart>,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        hearts.forEach { heart ->
            key(heart.id) {
                AnimatedHeartItem(heart = heart)
            }
        }
    }
}

@Composable
fun BoxScope.AnimatedHeartItem(heart: FloatingHeart) {
    val animProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1600, easing = LinearOutSlowInEasing)
        )
    }

    val progress = animProgress.value
    val yOffset = (-380.dp) * progress
    val xOffset = ((progress * 3.14159f * 4).let { kotlin.math.sin(it) } * 20).dp
    val alpha = (1f - progress).coerceIn(0f, 1f)
    val scale = (0.5f + (progress * 0.8f)).coerceAtMost(1.3f)

    Box(
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(end = (heart.startXFraction * 120).dp, bottom = 80.dp)
            .offset(x = xOffset, y = yOffset)
            .alpha(alpha)
    ) {
        val emojis = listOf("💖", "❤️", "🔥", "✨", "👑", "🌹")
        val emoji = emojis[(heart.id % emojis.size).toInt()]
        Text(
            text = emoji,
            fontSize = (heart.sizeDp * scale).sp
        )
    }
}
