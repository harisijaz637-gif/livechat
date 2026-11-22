package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PkOpponent
import com.example.ui.theme.*

@Composable
fun PkBattleVsBar(
    hostScore: Int,
    opponentScore: Int,
    timeRemainingSec: Int,
    opponent: PkOpponent?,
    modifier: Modifier = Modifier
) {
    val total = (hostScore + opponentScore).coerceAtLeast(1)
    val hostFraction = (hostScore.toFloat() / total.toFloat()).coerceIn(0.15f, 0.85f)
    val animatedFraction by animateFloatAsState(targetValue = hostFraction, label = "pkScoreBar")

    val minutes = timeRemainingSec / 60
    val seconds = timeRemainingSec % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Opponent info capsule + Timer
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Host Score
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text("👑 Host", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PoppoCyan)
                Text("$hostScore", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color.White)
            }

            // PK Timer Capsule
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(PoppoOverlayDark)
                    .border(1.dp, PoppoGold.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "⚔️ PK $timeFormatted",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = PoppoGoldLight
                )
            }

            // Opponent Score
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text("$opponentScore", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color.White)
                Text("${opponent?.name ?: "Opponent"} ${opponent?.countryFlag ?: ""}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PoppoPinkAccent)
            }
        }

        // Animated Dual Health/Score Bar with Center VS
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(Color.Black.copy(alpha = 0.5f))
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                // Host bar (Blue/Cyan/Violet)
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(animatedFraction)
                        .background(
                            Brush.horizontalGradient(
                                listOf(PoppoVioletPrimary, PoppoCyan)
                            )
                        )
                )
                // Opponent bar (Red/Pink)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                listOf(PoppoPinkAccent, PoppoRed)
                            )
                        )
                )
            }

            // Center VS Badge
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(PoppoGold)
                    .border(1.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "VS",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.Black
                )
            }
        }
    }
}
