package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LiveStream
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.PoppoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveRoomScreen(
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
    val floatingHearts by viewModel.floatingHearts.collectAsState()

    var commentText by remember { mutableStateOf("") }

    // Ambient animated spotlight
    val infiniteTransition = rememberInfiniteTransition(label = "stageLight")
    val stagePulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "stagePulse"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PoppoBackground)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        viewModel.addFloatingHeart()
                    }
                )
            }
    ) {
        // Simulated Live Stream Visual Backdrop
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(stream.primaryColorHex).copy(alpha = 0.55f * stagePulse),
                            Color(stream.secondaryColorHex).copy(alpha = 0.35f),
                            PoppoBackground
                        )
                    )
                )
        ) {
            // Live Performer Stage Centerpiece
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = (-40).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(stream.primaryColorHex),
                                    Color(stream.secondaryColorHex)
                                )
                            )
                        )
                        .border(3.dp, PoppoGold, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stream.hostAvatar,
                        fontSize = 64.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = stream.hostName,
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                    color = Color.White
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(PoppoOverlayDark)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "💎 ${stream.diamondsEarned} Diamonds",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = PoppoGoldLight
                        )
                    }
                }
            }
        }

        // Top HUD Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Host Capsule (Avatar, Name, Follow)
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.Black.copy(alpha = 0.65f))
                        .padding(horizontal = 6.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(PoppoVioletPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stream.hostAvatar, fontSize = 16.sp)
                    }
                    Column {
                        Text(
                            text = stream.hostName,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "ID: ${stream.id.hashCode().toString().takeLast(6)}",
                            fontSize = 9.sp,
                            color = PoppoTextMuted
                        )
                    }

                    // Follow Button
                    Button(
                        onClick = { viewModel.toggleFollow(stream.id) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (stream.isFollowed) PoppoSurfaceVariant else PoppoPinkAccent
                        ),
                        shape = RoundedCornerShape(14.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        modifier = Modifier.height(26.dp)
                    ) {
                        Text(
                            text = if (stream.isFollowed) "✓" else "+ Follow",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // Top Right Tools: Viewer count, Fans Leaderboard, Close
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Top 3 Fans Crowns
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.Black.copy(alpha = 0.5f))
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text("🥇", fontSize = 12.sp)
                        Text("🥈", fontSize = 12.sp)
                        Text("🥉", fontSize = 12.sp)
                    }

                    // Viewer Count
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.Black.copy(alpha = 0.5f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "👁️ ${stream.viewerCount}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    // Close Button
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.5f))
                            .testTag("btn_close_live_room")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Room",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // PK Battle VS Bar if active
            if (stream.isPkBattle) {
                Spacer(modifier = Modifier.height(8.dp))
                PkBattleVsBar(
                    hostScore = stream.pkScoreHost,
                    opponentScore = stream.pkScoreOpponent,
                    timeRemainingSec = stream.pkTimeRemainingSec,
                    opponent = stream.pkOpponent
                )
            }
        }

        // Floating Hearts Layer
        FloatingHeartsLayer(hearts = floatingHearts)

        // Live Chat Message Stream (Bottom-Left)
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(0.85f)
                .padding(start = 12.dp, bottom = 80.dp)
        ) {
            LazyColumn(
                reverseLayout = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 220.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(messages) { msg ->
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (msg.isGift) PoppoVioletDark.copy(alpha = 0.85f)
                                else if (msg.isSystem) PoppoOverlayDark.copy(alpha = 0.75f)
                                else Color.Black.copy(alpha = 0.6f)
                            )
                            .border(
                                0.5.dp,
                                if (msg.isGift) PoppoGold else Color.Transparent,
                                RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (!msg.isSystem) {
                            // User Level Badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(PoppoVioletPrimary)
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "Lv${msg.senderLevel}",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        // Sender Name
                        Text(
                            text = "${msg.senderName}:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = if (msg.isGift) PoppoGoldLight else if (msg.isSystem) PoppoCyan else PoppoPinkAccent
                        )

                        // Message Text or Gift
                        Text(
                            text = msg.text,
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Bottom Action Bar: Comment Input, Wheel, Like Heart, Gift Tray FAB
        Surface(
            color = Color.Black.copy(alpha = 0.75f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Comment Input Field
                OutlinedTextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    placeholder = { Text("Say something...", fontSize = 12.sp, color = PoppoTextMuted) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = PoppoSurfaceVariant,
                        unfocusedContainerColor = PoppoSurfaceVariant,
                        focusedBorderColor = PoppoPinkAccent,
                        unfocusedBorderColor = PoppoSurfaceBorder
                    ),
                    shape = RoundedCornerShape(20.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (commentText.isNotBlank()) {
                                viewModel.sendLiveComment(commentText)
                                commentText = ""
                            }
                        }
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("input_live_comment")
                )

                // Lucky Wheel Mini Game Icon
                IconButton(
                    onClick = { viewModel.openLuckyWheel() },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(PoppoSurfaceVariant)
                ) {
                    Text("🎰", fontSize = 18.sp)
                }

                // Send Like Heart Button
                IconButton(
                    onClick = { viewModel.addFloatingHeart() },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(PoppoSurfaceVariant)
                ) {
                    Text("💖", fontSize = 18.sp)
                }

                // Big Gift Box FAB (Triggers Gift Tray)
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(PoppoPinkAccent, PoppoGold)
                            )
                        )
                        .border(1.5.dp, Color.White, CircleShape)
                        .clickable { viewModel.openGiftSheet() }
                        .testTag("btn_open_gifts"),
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
