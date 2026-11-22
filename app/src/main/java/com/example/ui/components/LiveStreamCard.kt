package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LiveStream
import com.example.ui.theme.*

@Composable
fun LiveStreamCard(
    stream: LiveStream,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Pulsing live indicator
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(
                1.dp,
                Brush.linearGradient(
                    listOf(
                        Color(stream.primaryColorHex).copy(alpha = 0.6f),
                        Color(stream.secondaryColorHex).copy(alpha = 0.3f)
                    )
                ),
                RoundedCornerShape(16.dp)
            )
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(stream.primaryColorHex).copy(alpha = 0.45f),
                        PoppoSurface
                    )
                )
            )
            .clickable { onClick() }
            .testTag("card_stream_${stream.id}")
    ) {
        // Top Badges Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Live Status Indicator
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black.copy(alpha = 0.6f))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(PoppoRed.copy(alpha = alphaAnim))
                )
                Text(
                    text = "LIVE",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }

            // Viewer Count & Flag
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black.copy(alpha = 0.6f))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(stream.countryFlag, fontSize = 11.sp)
                Text("👁️", fontSize = 10.sp)
                Text(
                    text = if (stream.viewerCount >= 1000) "${String.format("%.1f", stream.viewerCount / 1000f)}k" else "${stream.viewerCount}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Center Visual Indicator (Avatar / PK / Party badge)
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(72.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(
                            Color(stream.primaryColorHex).copy(alpha = 0.8f),
                            Color(stream.secondaryColorHex).copy(alpha = 0.2f)
                        )
                    )
                )
                .border(2.dp, PoppoGoldLight, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stream.hostAvatar,
                fontSize = 36.sp
            )
        }

        // Feature Ribbon (PK Battle or 9-Seat Party)
        if (stream.isPkBattle) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = 10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(PoppoRed, PoppoVioletPrimary)
                        )
                    )
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "⚔️ PK BATTLE",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }
        } else if (stream.isPartyRoom) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = 10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(PoppoCyan, PoppoVioletPrimary)
                        )
                    )
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "🎙️ 9-SEAT PARTY",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }
        }

        // Bottom Info Bar with Host name & Title
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.9f))
                    )
                )
                .padding(horizontal = 10.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stream.hostName,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (stream.hotRank <= 3) {
                    Text("🔥 Top ${stream.hotRank}", fontSize = 10.sp, color = PoppoGoldLight)
                }
            }
            Text(
                text = stream.title,
                fontSize = 11.sp,
                color = PoppoTextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
