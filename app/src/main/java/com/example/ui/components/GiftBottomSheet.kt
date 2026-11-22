package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GiftItem
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GiftBottomSheet(
    gifts: List<GiftItem>,
    userCoins: Long,
    onSendGift: (GiftItem, Int) -> Unit,
    onOpenRecharge: () -> Unit,
    onDismiss: () -> Unit
) {
    var selectedGift by remember { mutableStateOf(gifts.firstOrNull()) }
    var selectedCount by remember { mutableIntStateOf(1) }
    val countOptions = listOf(1, 5, 10, 66, 1314)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = PoppoSurface,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(PoppoSurfaceBorder)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp)
        ) {
            // Header: "Send Gift" and Wallet Balance
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "GIFT TRAY",
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp,
                    color = Color.White
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .clickable { onOpenRecharge() }
                        .clip(RoundedCornerShape(14.dp))
                        .background(PoppoSurfaceVariant)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("🪙", fontSize = 13.sp)
                    Text(
                        text = "$userCoins",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = PoppoGoldLight
                    )
                    Text("Recharge >", fontSize = 11.sp, color = PoppoCyan)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Gifts Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                items(gifts) { gift ->
                    val isSelected = selectedGift?.id == gift.id
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected) PoppoVioletPrimary.copy(alpha = 0.25f)
                                else PoppoSurfaceVariant.copy(alpha = 0.5f)
                            )
                            .border(
                                1.5.dp,
                                if (isSelected) PoppoPinkAccent else Color.Transparent,
                                RoundedCornerShape(12.dp)
                            )
                            .clickable { selectedGift = gift }
                            .padding(8.dp)
                            .testTag("gift_${gift.id}"),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(gift.emoji, fontSize = 32.sp)
                        Text(
                            text = gift.name,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text("🪙", fontSize = 10.sp)
                            Text(
                                text = "${gift.coins}",
                                fontSize = 10.sp,
                                color = PoppoGoldLight,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Multiplier Combo count selector & Send Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Combo Count Pills
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    countOptions.forEach { count ->
                        val isCountSelected = selectedCount == count
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isCountSelected) PoppoPinkAccent else PoppoSurfaceVariant
                                )
                                .clickable { selectedCount = count }
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "x$count",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isCountSelected) Color.White else PoppoTextMuted
                            )
                        }
                    }
                }

                // Send Button
                Button(
                    onClick = {
                        selectedGift?.let { gift ->
                            onSendGift(gift, selectedCount)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PoppoPinkAccent),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                    modifier = Modifier.testTag("btn_send_gift")
                ) {
                    Text(
                        text = "SEND",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}
