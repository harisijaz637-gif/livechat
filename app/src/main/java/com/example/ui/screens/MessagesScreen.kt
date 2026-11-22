package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.ui.theme.*
import com.example.ui.viewmodel.PoppoScreen
import com.example.ui.viewmodel.PoppoViewModel

@Composable
fun MessagesScreen(
    viewModel: PoppoViewModel,
    modifier: Modifier = Modifier
) {
    val dms by viewModel.repository.directMessages.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PoppoBackground)
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "MESSAGES 💬",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(PoppoSurfaceVariant)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "4 Chats",
                    fontSize = 11.sp,
                    color = PoppoCyan,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Official System Banner
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(PoppoVioletDark, PoppoSurfaceVariant)
                    )
                )
                .border(1.dp, PoppoGold.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(PoppoGold),
                contentAlignment = Alignment.Center
            ) {
                Text("📢", fontSize = 20.sp)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Official Poppo Notice",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = PoppoGoldLight
                )
                Text(
                    text = "Claim your Daily Check-in bonus and spin the Lucky Wheel for free coins today!",
                    fontSize = 11.sp,
                    color = PoppoTextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Direct Chats",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        // DMs List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(dms) { dm ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(PoppoSurface)
                        .border(1.dp, PoppoSurfaceBorder, RoundedCornerShape(14.dp))
                        .clickable {
                            viewModel.navigateTo(
                                PoppoScreen.DirectChat(
                                    hostId = dm.hostId,
                                    hostName = dm.hostName,
                                    hostAvatar = dm.hostAvatar
                                )
                            )
                        }
                        .padding(12.dp)
                        .testTag("dm_item_${dm.hostId}"),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Avatar with online status
                    Box(modifier = Modifier.size(46.dp)) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(PoppoSurfaceVariant)
                                .border(1.5.dp, PoppoPinkAccent, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(dm.hostAvatar, fontSize = 22.sp)
                        }
                        if (dm.isOnline) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(PoppoGreen)
                                    .border(1.5.dp, PoppoSurface, CircleShape)
                            )
                        }
                    }

                    // Content
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = dm.hostName,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(dm.countryFlag, fontSize = 11.sp)
                            }
                            Text(
                                text = dm.timeAgo,
                                fontSize = 10.sp,
                                color = PoppoTextMuted
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = dm.lastMessage,
                            fontSize = 11.sp,
                            color = PoppoTextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    if (dm.unreadCount > 0) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(PoppoPinkAccent),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${dm.unreadCount}",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
