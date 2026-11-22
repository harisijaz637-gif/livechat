package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material3.Icon
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
import com.example.data.model.PartySeat
import com.example.ui.theme.*

@Composable
fun PartyNineGridSeats(
    seats: List<PartySeat>,
    onSeatClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // Speaking wave animation
    val infiniteTransition = rememberInfiniteTransition(label = "pulseSpeaking")
    val waveScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "waveScale"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(PoppoOverlayDark.copy(alpha = 0.85f))
            .border(1.dp, PoppoSurfaceBorder, RoundedCornerShape(20.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 3 rows of 3 seats = 9 seats
        for (row in 0..2) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (col in 0..2) {
                    val index = row * 3 + col
                    val seat = seats.find { it.seatIndex == index } ?: PartySeat(index)
                    PartySeatItem(
                        seat = seat,
                        waveScale = if (seat.isSpeaking) waveScale else 1f,
                        onClick = { onSeatClick(index) }
                    )
                }
            }
        }
    }
}

@Composable
fun PartySeatItem(
    seat: PartySeat,
    waveScale: Float,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clickable { onClick() }
            .testTag("seat_${seat.seatIndex}")
    ) {
        Box(
            modifier = Modifier.size(54.dp),
            contentAlignment = Alignment.Center
        ) {
            // Speaking wave aura
            if (seat.isSpeaking && seat.userName != null) {
                Box(
                    modifier = Modifier
                        .size(54.dp * waveScale)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(PoppoCyan.copy(alpha = 0.4f), Color.Transparent)
                            )
                        )
                )
            }

            // Seat Avatar or Empty Slot
            if (seat.userName != null) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(
                            if (seat.isHost) Brush.linearGradient(listOf(PoppoGold, PoppoPinkAccent))
                            else Brush.linearGradient(listOf(PoppoVioletPrimary, PoppoSurfaceVariant))
                        )
                        .border(
                            2.dp,
                            if (seat.isHost) PoppoGold else if (seat.isSpeaking) PoppoCyan else PoppoSurfaceBorder,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = seat.userAvatar ?: "👤",
                        fontSize = 20.sp
                    )
                }

                // Host Crown or Mic Status Badge
                if (seat.isHost) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .offset(y = (-6).dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(PoppoGold)
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text("HOST", fontSize = 7.sp, fontWeight = FontWeight.Black, color = Color.Black)
                    }
                }

                // Mic indicator at bottom
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(if (seat.isMuted) PoppoRed else PoppoGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (seat.isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = "Mic Status",
                        tint = Color.White,
                        modifier = Modifier.size(10.dp)
                    )
                }
            } else {
                // Empty Seat Slot
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(PoppoSurfaceVariant.copy(alpha = 0.7f))
                        .border(1.dp, PoppoSurfaceBorder.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Take Seat",
                        tint = PoppoTextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Seat label / Username
        Text(
            text = seat.userName ?: "Seat ${seat.seatIndex + 1}",
            fontSize = 10.sp,
            fontWeight = if (seat.userName != null) FontWeight.Bold else FontWeight.Normal,
            color = if (seat.isHost) PoppoGoldLight else if (seat.userName != null) Color.White else PoppoTextMuted,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.widthIn(max = 64.dp)
        )
    }
}
