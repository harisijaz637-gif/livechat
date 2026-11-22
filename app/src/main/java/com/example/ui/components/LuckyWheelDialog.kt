package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun LuckyWheelDialog(
    freeSpins: Int,
    onSpinClaimed: () -> Unit,
    onDismiss: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var isSpinning by remember { mutableStateOf(false) }
    var currentRotation by remember { mutableFloatStateOf(0f) }
    val rotationAnim = remember { Animatable(0f) }

    val prizes = listOf("50", "100", "250", "500", "1000", "2500")
    val sectorColors = listOf(
        PoppoVioletPrimary,
        PoppoPinkAccent,
        PoppoGold,
        PoppoCyan,
        PoppoVioletDark,
        Color(0xFFFF5722)
    )

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
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "LUCKY WHEEL 🎰",
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Spin & Win Free Gold Coins!",
                            fontSize = 12.sp,
                            color = PoppoGoldLight
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = PoppoTextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Wheel Container with Pointer
                Box(
                    modifier = Modifier.size(240.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Wheel Canvas
                    Canvas(
                        modifier = Modifier
                            .size(220.dp)
                            .rotate(rotationAnim.value)
                    ) {
                        val sectorAngle = 360f / prizes.size
                        for (i in prizes.indices) {
                            drawArc(
                                color = sectorColors[i % sectorColors.size],
                                startAngle = i * sectorAngle,
                                sweepAngle = sectorAngle,
                                useCenter = true,
                                topLeft = Offset.Zero,
                                size = Size(size.width, size.height),
                                style = Fill
                            )
                        }
                    }

                    // Center Hub Button
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(PoppoSurface)
                            .border(2.dp, PoppoGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "SPIN",
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            color = PoppoGoldLight
                        )
                    }

                    // Top Pointer
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .offset(y = (-8).dp)
                    ) {
                        Text("🔻", fontSize = 24.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Free Spins Status
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("Free Spins Remaining:", fontSize = 13.sp, color = PoppoTextSecondary)
                    Text(
                        text = "$freeSpins",
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp,
                        color = if (freeSpins > 0) PoppoGreen else PoppoRed
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Spin Action Button
                Button(
                    onClick = {
                        if (!isSpinning && freeSpins > 0) {
                            isSpinning = true
                            coroutineScope.launch {
                                val target = currentRotation + 1440f + Random.nextInt(0, 360)
                                rotationAnim.animateTo(
                                    targetValue = target,
                                    animationSpec = tween(
                                        durationMillis = 2800,
                                        easing = FastOutSlowInEasing
                                    )
                                )
                                currentRotation = target % 360f
                                isSpinning = false
                                onSpinClaimed()
                            }
                        }
                    },
                    enabled = !isSpinning && freeSpins > 0,
                    colors = ButtonDefaults.buttonColors(containerColor = PoppoGold),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_spin_wheel")
                ) {
                    Text(
                        text = if (isSpinning) "SPINNING..." else if (freeSpins > 0) "SPIN NOW! 🎰" else "NO SPINS LEFT",
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}
