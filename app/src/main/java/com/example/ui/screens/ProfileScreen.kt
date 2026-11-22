package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.PoppoViewModel

@Composable
fun ProfileScreen(
    viewModel: PoppoViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.repository.userProfile.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(PoppoBackground)
            .statusBarsPadding()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Profile Header Card
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Avatar with VIP Golden Crown Frame
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(PoppoGold, PoppoPinkAccent)
                                )
                            )
                            .border(3.dp, PoppoGoldLight, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userProfile.avatar,
                            fontSize = 38.sp
                        )
                        // VIP Badge at corner
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .clip(RoundedCornerShape(8.dp))
                                .background(PoppoPinkAccent)
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        ) {
                            Text("VIP ${userProfile.vipLevel}", fontSize = 8.sp, fontWeight = FontWeight.Black, color = Color.White)
                        }
                    }

                    // User Info
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = userProfile.name,
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp,
                                color = Color.White
                            )
                            Text("👑", fontSize = 16.sp)
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Poppo ID: ${userProfile.poppoId}",
                                fontSize = 11.sp,
                                color = PoppoTextMuted
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(PoppoSurfaceVariant)
                                    .clickable { viewModel.showToast("ID copied to clipboard!") }
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                Text("COPY", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = PoppoCyan)
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Level Badges
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(PoppoVioletPrimary)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("Wealth Lv.${userProfile.wealthLevel}", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(PoppoPinkAccent)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("Charm Lv.${userProfile.charmLevel}", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Bio
                Text(
                    text = userProfile.bio,
                    fontSize = 12.sp,
                    color = PoppoTextSecondary
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Stats: Following / Followers
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(PoppoSurface)
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${userProfile.followingCount}",
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                        Text("Following", fontSize = 11.sp, color = PoppoTextMuted)
                    }

                    Box(modifier = Modifier.width(1.dp).height(24.dp).background(PoppoSurfaceBorder))

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${userProfile.followersCount}",
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                        Text("Followers", fontSize = 11.sp, color = PoppoTextMuted)
                    }

                    Box(modifier = Modifier.width(1.dp).height(24.dp).background(PoppoSurfaceBorder))

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "VIP ${userProfile.vipLevel}",
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            color = PoppoGoldLight
                        )
                        Text("Privilege", fontSize = 11.sp, color = PoppoTextMuted)
                    }
                }
            }
        }

        // Wallet Overview Card
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(PoppoSurfaceVariant, PoppoSurface)
                        )
                    )
                    .border(1.dp, PoppoSurfaceBorder, RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "MY WALLET 💰",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = Color.White
                    )

                    Button(
                        onClick = { viewModel.openRecharge() },
                        colors = ButtonDefaults.buttonColors(containerColor = PoppoGold),
                        shape = RoundedCornerShape(14.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                        modifier = Modifier.height(30.dp).testTag("btn_profile_recharge")
                    ) {
                        Text("TOP-UP", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color.Black)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Gold Coins
                    Column {
                        Text("Gold Coins", fontSize = 11.sp, color = PoppoTextMuted)
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("🪙", fontSize = 16.sp)
                            Text(
                                text = "${userProfile.coins}",
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp,
                                color = PoppoGoldLight
                            )
                        }
                    }

                    // Diamonds Earned
                    Column {
                        Text("Diamonds Earned", fontSize = 11.sp, color = PoppoTextMuted)
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("💎", fontSize = 16.sp)
                            Text(
                                text = "${userProfile.diamonds}",
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp,
                                color = PoppoCyan
                            )
                        }
                    }
                }
            }
        }

        // Feature Shortcuts Grid
        item {
            Text(
                text = "Entertainment & Privileges",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Lucky Wheel Tile
                ProfileMenuItem(
                    icon = "🎰",
                    title = "Lucky Wheel Game",
                    subtitle = "${userProfile.freeWheelSpins} Free spins available",
                    onClick = { viewModel.openLuckyWheel() }
                )

                // Daily Tasks Tile
                ProfileMenuItem(
                    icon = "🎁",
                    title = "Task & Reward Center",
                    subtitle = "Streak: ${userProfile.checkInStreak} days • Claim free coins",
                    onClick = { viewModel.openDailyTask() }
                )

                // Backpack Tile
                ProfileMenuItem(
                    icon = "🎒",
                    title = "My Backpack & Equips",
                    subtitle = "VIP Crown Frame, Supercar Entrance Ride",
                    onClick = { viewModel.showToast("Equipped: Golden Crown Avatar Frame & Supercar Ride ✨") }
                )

                // VIP Privilege Center
                ProfileMenuItem(
                    icon = "👑",
                    title = "VIP Privilege Hall",
                    subtitle = "Level VIP 3 • Exclusive animated chat bubbles",
                    onClick = { viewModel.showToast("VIP 3 Active: Free entry effects & purple name tag!") }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: String,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(PoppoSurface)
            .border(1.dp, PoppoSurfaceBorder, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(PoppoSurfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(icon, fontSize = 20.sp)
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = PoppoTextMuted
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Open",
            tint = PoppoTextMuted
        )
    }
}
