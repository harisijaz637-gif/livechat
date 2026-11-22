package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GiftAnimType
import com.example.data.model.GiftItem
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun GiftOverlayAnimation(
    gift: GiftItem?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (gift == null) return

    val animProgress = remember { Animatable(0f) }

    LaunchedEffect(gift) {
        animProgress.snapTo(0f)
        animProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = if (gift.animType == GiftAnimType.DRAGON_FIREWORKS || gift.animType == GiftAnimType.SUPERCAR_RUSH) 2800 else 1800,
                easing = FastOutSlowInEasing
            )
        )
        delay(200)
        onDismiss()
    }

    val progress = animProgress.value

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = (0.4f * (1f - (progress - 0.7f).coerceAtLeast(0f) / 0.3f)))),
        contentAlignment = Alignment.Center
    ) {
        // Luxury Gift Top Announcement Banner
        val bannerAlpha = if (progress < 0.2f) progress / 0.2f else if (progress > 0.8f) (1f - progress) / 0.2f else 1f
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 100.dp)
                .alpha(bannerAlpha)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(PoppoGold, PoppoPinkAccent, PoppoVioletPrimary)
                    )
                )
                .padding(horizontal = 18.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("🌟", fontSize = 16.sp)
                Text(
                    text = "GIFT COMBO: ${gift.name.uppercase()} ${gift.emoji}",
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    color = Color.White
                )
                Text("🌟", fontSize = 16.sp)
            }
        }

        // Center Content based on anim type
        when (gift.animType) {
            GiftAnimType.SUPERCAR_RUSH -> {
                // Car zooming across screen
                val carXOffset = (350.dp) * (1f - progress * 2.2f)
                val carScale = 1.1f
                Box(
                    modifier = Modifier
                        .offset(x = carXOffset)
                        .scale(carScale)
                        .size(300.dp)
                        .clip(RoundedCornerShape(24.dp))
                ) {
                    if (gift.drawableRes != null) {
                        Image(
                            painter = painterResource(id = gift.drawableRes),
                            contentDescription = gift.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text("🏎️💨", fontSize = 120.sp, modifier = Modifier.align(Alignment.Center))
                    }
                }
            }
            GiftAnimType.DRAGON_FIREWORKS -> {
                // Majestic Scaling Dragon with golden glow
                val dragonScale = (0.6f + (progress * 0.7f)).coerceAtMost(1.25f)
                val dragonAlpha = if (progress > 0.85f) (1f - progress) / 0.15f else 1f

                Box(
                    modifier = Modifier
                        .scale(dragonScale)
                        .alpha(dragonAlpha)
                        .size(320.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .border(3.dp, PoppoGoldLight, RoundedCornerShape(32.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (gift.drawableRes != null) {
                        Image(
                            painter = painterResource(id = gift.drawableRes),
                            contentDescription = gift.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text("🐉✨", fontSize = 140.sp)
                    }
                }
            }
            else -> {
                // Burst / Bounce for Rose, Heart, Teddy, Mic
                val bounceScale = (1.5f * kotlin.math.sin(progress * 3.14159f)).coerceIn(0.5f, 1.8f)
                val bounceAlpha = (1f - progress).coerceIn(0f, 1f)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .scale(bounceScale.toFloat())
                        .alpha(bounceAlpha)
                ) {
                    Text(gift.emoji, fontSize = 90.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${gift.name} x1",
                        fontWeight = FontWeight.Black,
                        fontSize = 22.sp,
                        color = PoppoGoldLight
                    )
                }
            }
        }
    }
}
