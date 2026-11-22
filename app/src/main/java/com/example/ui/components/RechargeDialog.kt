package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.ui.theme.*

data class RechargePackage(
    val coins: Long,
    val bonus: Long,
    val priceStr: String,
    val isBestValue: Boolean = false,
    val tag: String? = null
)

@Composable
fun RechargeDialog(
    userCoins: Long,
    onRechargeSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val packages = listOf(
        RechargePackage(500, 50, "$0.99", false, "+10% BONUS"),
        RechargePackage(2000, 400, "$3.99", false, "+20% HOT"),
        RechargePackage(5500, 1500, "$9.99", true, "BEST VALUE"),
        RechargePackage(15000, 5000, "$29.99", false, "VIP SPECIAL"),
        RechargePackage(60000, 25000, "$99.99", false, "SULTAN PACK")
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
                            text = "WALLET RECHARGE",
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Current: $userCoins Coins 🪙",
                            fontSize = 12.sp,
                            color = PoppoGoldLight
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = PoppoTextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Packages List
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(packages) { pack ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    if (pack.isBestValue) PoppoVioletPrimary.copy(alpha = 0.25f)
                                    else PoppoSurfaceVariant
                                )
                                .border(
                                    1.dp,
                                    if (pack.isBestValue) PoppoGold else PoppoSurfaceBorder,
                                    RoundedCornerShape(14.dp)
                                )
                                .clickable { onRechargeSelected(pack.coins + pack.bonus) }
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                                .testTag("recharge_${pack.coins}"),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text("🪙", fontSize = 16.sp)
                                    Text(
                                        text = "${pack.coins} Coins",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = Color.White
                                    )
                                    pack.tag?.let { tag ->
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(if (pack.isBestValue) PoppoGold else PoppoPinkAccent)
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = tag,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Black,
                                                color = Color.Black
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = "+${pack.bonus} Free Bonus Coins",
                                    fontSize = 11.sp,
                                    color = PoppoGoldLight
                                )
                            }

                            Button(
                                onClick = { onRechargeSelected(pack.coins + pack.bonus) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (pack.isBestValue) PoppoGold else PoppoVioletPrimary
                                ),
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = pack.priceStr,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (pack.isBestValue) Color.Black else Color.White
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "🔒 Safe & Instant simulated credit to your Poppo ID.",
                    fontSize = 11.sp,
                    color = PoppoTextMuted,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}
