package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun PoppoTopBar(
    userCoins: Long,
    onOpenRecharge: () -> Unit,
    onOpenLuckyWheel: () -> Unit,
    onOpenDailyTask: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App Title / Brand
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(PoppoPinkAccent, PoppoVioletPrimary)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "👑",
                    fontSize = 20.sp
                )
            }
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "LIVECHAT",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(PoppoPinkAccent)
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "LIVE",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
                Text(
                    text = "VIP Entertainment",
                    fontSize = 10.sp,
                    color = PoppoTextMuted
                )
            }
        }

        // Right side tools: Lucky Wheel, Daily Tasks, Coin Wallet
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Lucky Wheel Shortcut
            IconButton(
                onClick = onOpenLuckyWheel,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(PoppoSurfaceVariant)
                    .testTag("btn_lucky_wheel")
            ) {
                Text("🎰", fontSize = 16.sp)
            }

            // Daily Tasks Shortcut
            IconButton(
                onClick = onOpenDailyTask,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(PoppoSurfaceVariant)
                    .testTag("btn_daily_task")
            ) {
                Text("🎁", fontSize = 16.sp)
            }

            // Coin Wallet Pill
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(PoppoSurfaceVariant, PoppoSurface)
                        )
                    )
                    .border(1.dp, PoppoGold.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                    .clickable { onOpenRecharge() }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .testTag("pill_coin_wallet"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text("🪙", fontSize = 14.sp)
                Text(
                    text = if (userCoins >= 10000) "${userCoins / 1000}k" else "$userCoins",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = PoppoGoldLight
                )
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(PoppoGold),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Recharge Coins",
                        tint = Color.Black,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}
