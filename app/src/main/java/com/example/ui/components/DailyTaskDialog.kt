package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.window.Dialog
import com.example.data.model.DailyTask
import com.example.ui.theme.*

@Composable
fun DailyTaskDialog(
    tasks: List<DailyTask>,
    checkedInToday: Boolean,
    streak: Int,
    onClaimCheckIn: () -> Unit,
    onClaimTask: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val checkInRewards = listOf(100, 150, 200, 250, 350, 500, 1000)

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = PoppoSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, PoppoSurfaceBorder),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "REWARD CENTER 🎁",
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Streak: $streak Days | Daily Bonuses",
                            fontSize = 12.sp,
                            color = PoppoGoldLight
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = PoppoTextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 7-Day Check-in Row
                Text(
                    text = "7-Day Login Calendar",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    for (day in 1..7) {
                        val isPast = day < streak || (day == streak && checkedInToday)
                        val isToday = day == streak && !checkedInToday
                        val reward = checkInRewards[day - 1]

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 2.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isToday) PoppoPinkAccent.copy(alpha = 0.3f)
                                    else if (isPast) PoppoGreen.copy(alpha = 0.2f)
                                    else PoppoSurfaceVariant
                                )
                                .border(
                                    1.dp,
                                    if (isToday) PoppoPinkAccent else if (isPast) PoppoGreen else Color.Transparent,
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(vertical = 6.dp)
                        ) {
                            Text("D$day", fontSize = 9.sp, color = PoppoTextMuted)
                            Text(if (isPast) "✓" else "🪙", fontSize = 11.sp)
                            Text("+$reward", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = PoppoGoldLight)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onClaimCheckIn,
                    enabled = !checkedInToday,
                    colors = ButtonDefaults.buttonColors(containerColor = PoppoPinkAccent),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_daily_checkin")
                ) {
                    Text(
                        text = if (checkedInToday) "CHECKED IN TODAY ✓" else "CHECK IN NOW (+${checkInRewards[(streak - 1).coerceIn(0, 6)]} COINS)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Daily Tasks
                Text(
                    text = "Daily Missions",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                ) {
                    items(tasks) { task ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(PoppoSurfaceVariant)
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = task.title,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text("🪙 +${task.rewardCoins}", fontSize = 11.sp, color = PoppoGoldLight)
                                    Text("•", fontSize = 10.sp, color = PoppoTextMuted)
                                    Text("${task.currentProgress}/${task.maxProgress}", fontSize = 11.sp, color = PoppoCyan)
                                }
                            }

                            Button(
                                onClick = { onClaimTask(task.id) },
                                enabled = !task.isClaimed && task.currentProgress >= task.maxProgress,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (task.isClaimed) PoppoSurfaceBorder else PoppoGold
                                ),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                modifier = Modifier.testTag("task_claim_${task.id}")
                            ) {
                                Text(
                                    text = if (task.isClaimed) "CLAIMED" else "CLAIM",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (task.isClaimed) PoppoTextMuted else Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
