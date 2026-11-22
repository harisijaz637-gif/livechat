package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.data.model.LiveCategory
import com.example.ui.components.LiveStreamCard
import com.example.ui.components.PoppoTopBar
import com.example.ui.theme.*
import com.example.ui.viewmodel.PoppoScreen
import com.example.ui.viewmodel.PoppoViewModel

@Composable
fun HomeScreen(
    viewModel: PoppoViewModel,
    modifier: Modifier = Modifier
) {
    val streams by viewModel.repository.streams.collectAsState()
    val userProfile by viewModel.repository.userProfile.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    val filteredStreams = streams.filter { stream ->
        when (selectedCategory) {
            LiveCategory.POPULAR -> true
            LiveCategory.PK -> stream.isPkBattle
            LiveCategory.PARTY -> stream.isPartyRoom
            LiveCategory.TALENT -> stream.category == LiveCategory.TALENT
            LiveCategory.FOLLOWED -> stream.isFollowed
        }
    }

    Box(modifier = modifier.fillMaxSize().background(PoppoBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Bar
            PoppoTopBar(
                userCoins = userProfile.coins,
                onOpenRecharge = { viewModel.openRecharge() },
                onOpenLuckyWheel = { viewModel.openLuckyWheel() },
                onOpenDailyTask = { viewModel.openDailyTask() }
            )

            // Category Navigation Tabs
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(LiveCategory.values()) { category ->
                    val isSelected = category == selectedCategory
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (isSelected) Brush.horizontalGradient(listOf(PoppoPinkAccent, PoppoVioletPrimary))
                                else Brush.horizontalGradient(listOf(PoppoSurfaceVariant, PoppoSurface))
                            )
                            .border(
                                1.dp,
                                if (isSelected) PoppoPinkAccent else PoppoSurfaceBorder,
                                RoundedCornerShape(20.dp)
                            )
                            .clickable { viewModel.selectCategory(category) }
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                            .testTag("tab_${category.name}")
                    ) {
                        Text(
                            text = category.label,
                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                            fontSize = 12.sp,
                            color = if (isSelected) Color.White else PoppoTextMuted
                        )
                    }
                }
            }

            // Main Streams Feed
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 90.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                // Hero Banner
                item(span = { GridItemSpan(2) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.dp, PoppoSurfaceBorder, RoundedCornerShape(16.dp))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_live_hero_banner),
                            contentDescription = "Poppo Live Concert Showcase",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                                    )
                                )
                        )
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(PoppoGold)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "GLOBAL TALENT FESTIVAL",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.Black
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Win 1,000,000 Diamonds in PK League! 👑",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                // Empty state if followed has no items
                if (filteredStreams.isEmpty()) {
                    item(span = { GridItemSpan(2) }) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("👑", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No followed hosts are live right now",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Explore the Popular tab to discover amazing stars!",
                                fontSize = 12.sp,
                                color = PoppoTextMuted
                            )
                        }
                    }
                } else {
                    items(filteredStreams) { stream ->
                        LiveStreamCard(
                            stream = stream,
                            onClick = {
                                if (stream.isPartyRoom) {
                                    viewModel.navigateTo(PoppoScreen.PartyRoom(stream.id))
                                } else {
                                    viewModel.navigateTo(PoppoScreen.LiveRoom(stream.id))
                                }
                            }
                        )
                    }
                }
            }
        }

        // Floating "GO LIVE" Action Button with Glowing Ring
        FloatingActionButton(
            onClick = { viewModel.navigateTo(PoppoScreen.GoLive) },
            containerColor = PoppoPinkAccent,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-20).dp)
                .size(60.dp)
                .border(2.dp, PoppoGold, CircleShape)
                .testTag("fab_go_live")
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Videocam,
                    contentDescription = "Go Live",
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "LIVE",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}
