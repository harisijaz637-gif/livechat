package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.PoppoViewModel

@Composable
fun PartyRoomScreen(
    streamId: String,
    viewModel: PoppoViewModel,
    modifier: Modifier = Modifier
) {
    val streams by viewModel.repository.streams.collectAsState()
    val stream = streams.find { it.id == streamId } ?: return
    val userProfile by viewModel.repository.userProfile.collectAsState()
    val messages by viewModel.repository.liveRoomMessages.collectAsState()
    val activeGift by viewModel.repository.activeGiftAnimation.collectAsState()
    val showGiftSheet by viewModel.showGiftBottomSheet.collectAsState()

    var isMyMicMuted by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PoppoBackground)
    ) {
        // Party Club Background
        Image(
            painter = painterResource(id = R.drawable.img_party_stage),
            contentDescription = "Party Voice Room Stage",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Dark Gradient Scrim
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Black.copy(alpha = 0.7f),
                            Color.Black.copy(alpha = 0.5f),
                            Color.Black.copy(alpha = 0.95f)
                        )
                    )
                )
        )

        // Content Column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 14.dp)
        ) {
            // Room Top Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(PoppoVioletPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stream.hostAvatar, fontSize = 20.sp)
                    }
                    Column {
                        Text(
                            text = stream.title,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("ID: 802914", fontSize = 10.sp, color = PoppoTextMuted)
                            Text("•", fontSize = 10.sp, color = PoppoTextMuted)
                            Text("👥 ${stream.viewerCount} in room", fontSize = 10.sp, color = PoppoCyan)
                        }
                    }
                }

                IconButton(
                    onClick = { viewModel.navigateBack() },
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.6f))
                        .testTag("btn_close_party_room")
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Leave Room", tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 9-Seats Voice Grid
            PartyNineGridSeats(
                seats = stream.partySeats,
                onSeatClick = { seatIndex ->
                    viewModel.togglePartySeat(stream.id, seatIndex)
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Mini Games Quick Bar (Lucky Wheel, Dice, Treasure Chest)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(PoppoOverlayDark)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.clickable { viewModel.openLuckyWheel() }
                ) {
                    Text("🎰", fontSize = 16.sp)
                    Text("Lucky Wheel", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PoppoGoldLight)
                }

                Box(modifier = Modifier.width(1.dp).height(16.dp).background(PoppoSurfaceBorder))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.clickable { viewModel.showToast("Rolled a ⚅ 6! You lead the table!") }
                ) {
                    Text("🎲", fontSize = 16.sp)
                    Text("Roll Dice", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PoppoCyan)
                }

                Box(modifier = Modifier.width(1.dp).height(16.dp).background(PoppoSurfaceBorder))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.clickable { viewModel.showToast("Treasure Box drops in 01:24! Stay in room to claim coins!") }
                ) {
                    Text("📦", fontSize = 16.sp)
                    Text("Drop: 01:24", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PoppoPinkAccent)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Party Chat Messages
            LazyColumn(
                reverseLayout = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 140.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(messages) { msg ->
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.Black.copy(alpha = 0.55f))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "${msg.senderName}:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = PoppoCyan
                        )
                        Text(
                            text = msg.text,
                            fontSize = 11.sp,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bottom Controls Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mic Mute Toggle
                Button(
                    onClick = {
                        isMyMicMuted = !isMyMicMuted
                        viewModel.showToast(if (isMyMicMuted) "Mic Muted" else "Mic Active 🎙️")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isMyMicMuted) PoppoRed else PoppoGreen
                    ),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = if (isMyMicMuted) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = "Mic Toggle",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = if (isMyMicMuted) "MUTED" else "SPEAKING", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                // Gift Button
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(listOf(PoppoPinkAccent, PoppoGold))
                        )
                        .border(1.5.dp, Color.White, CircleShape)
                        .clickable { viewModel.openGiftSheet() }
                        .testTag("btn_party_gift"),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🎁", fontSize = 22.sp)
                }
            }
        }

        // Full Screen Animated Gift Trigger Overlay
        GiftOverlayAnimation(
            gift = activeGift,
            onDismiss = { viewModel.repository.dismissGiftAnimation() }
        )

        // Bottom Sheet Gift Tray
        if (showGiftSheet) {
            GiftBottomSheet(
                gifts = viewModel.repository.giftCatalog,
                userCoins = userProfile.coins,
                onSendGift = { gift, count ->
                    viewModel.sendGift(stream.id, gift, count)
                    viewModel.closeGiftSheet()
                },
                onOpenRecharge = {
                    viewModel.closeGiftSheet()
                    viewModel.openRecharge()
                },
                onDismiss = { viewModel.closeGiftSheet() }
            )
        }
    }
}
