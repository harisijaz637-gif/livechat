package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GiftBottomSheet
import com.example.ui.components.GiftOverlayAnimation
import com.example.ui.theme.*
import com.example.ui.viewmodel.PoppoViewModel

@Composable
fun DirectChatScreen(
    hostId: String,
    hostName: String,
    hostAvatar: String,
    viewModel: PoppoViewModel,
    modifier: Modifier = Modifier
) {
    val chatMap by viewModel.repository.chatMessagesMap.collectAsState()
    val messages = chatMap[hostId] ?: emptyList()
    val userProfile by viewModel.repository.userProfile.collectAsState()
    val activeGift by viewModel.repository.activeGiftAnimation.collectAsState()
    val showGiftSheet by viewModel.showGiftBottomSheet.collectAsState()

    var textInput by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PoppoBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Chat Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PoppoSurface)
                    .border(0.5.dp, PoppoSurfaceBorder, RoundedCornerShape(0.dp))
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = { viewModel.navigateBack() },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(PoppoVioletPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(hostAvatar, fontSize = 18.sp)
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = hostName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(PoppoGreen)
                        )
                        Text(
                            text = "Online now",
                            fontSize = 10.sp,
                            color = PoppoGreen
                        )
                    }
                }

                // Gift Button in chat
                Button(
                    onClick = { viewModel.openGiftSheet() },
                    colors = ButtonDefaults.buttonColors(containerColor = PoppoPinkAccent),
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(30.dp)
                ) {
                    Text("🎁 Send Gift", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            // Messages List
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(messages) { msg ->
                    val isMe = msg.isMe
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                    ) {
                        Box(
                            modifier = Modifier
                                .widthIn(max = 260.dp)
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 14.dp,
                                        topEnd = 14.dp,
                                        bottomStart = if (isMe) 14.dp else 2.dp,
                                        bottomEnd = if (isMe) 2.dp else 14.dp
                                    )
                                )
                                .background(if (isMe) PoppoVioletPrimary else PoppoSurfaceVariant)
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Column {
                                Text(
                                    text = msg.text,
                                    fontSize = 13.sp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = msg.timeStr,
                                    fontSize = 9.sp,
                                    color = if (isMe) PoppoTextSecondary else PoppoTextMuted,
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Input Bar
            Surface(
                color = PoppoSurface,
                modifier = Modifier
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
                    OutlinedTextField(
                        value = textInput,
                        onValueChange = { textInput = it },
                        placeholder = { Text("Write a message to $hostName...", fontSize = 12.sp, color = PoppoTextMuted) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = PoppoPinkAccent,
                            unfocusedBorderColor = PoppoSurfaceBorder
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("input_direct_chat")
                    )

                    IconButton(
                        onClick = {
                            if (textInput.isNotBlank()) {
                                viewModel.sendDirectChat(hostId, textInput)
                                textInput = ""
                            }
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(PoppoPinkAccent)
                            .testTag("btn_send_direct_chat")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // Animated Gift Trigger Overlay
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
                    viewModel.sendGift("stream_1", gift, count)
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
