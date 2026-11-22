package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LiveCategory
import com.example.ui.components.DailyTaskDialog
import com.example.ui.components.LuckyWheelDialog
import com.example.ui.components.RechargeDialog
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.PoppoScreen
import com.example.ui.viewmodel.PoppoViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: PoppoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                PoppoMainApp(viewModel = viewModel)
            }
        }
    }
}

enum class BottomNavTab {
    LIVE, PARTY, MESSAGES, PROFILE
}

@Composable
fun PoppoMainApp(viewModel: PoppoViewModel) {
    val context = LocalContext.current
    val currentScreen by viewModel.currentScreen.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

    val showRecharge by viewModel.showRechargeDialog.collectAsState()
    val showLuckyWheel by viewModel.showLuckyWheelDialog.collectAsState()
    val showDailyTask by viewModel.showDailyTaskDialog.collectAsState()
    val userProfile by viewModel.repository.userProfile.collectAsState()
    val dailyTasks by viewModel.repository.dailyTasks.collectAsState()

    var activeBottomTab by remember { mutableStateOf(BottomNavTab.LIVE) }

    // Toast listener
    LaunchedEffect(toastMessage) {
        toastMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    // BackHandler: If on detail screen, navigate back to Home
    BackHandler(enabled = currentScreen !is PoppoScreen.Home) {
        viewModel.navigateBack()
    }

    // Determine if bottom navigation should be visible
    val isBottomNavVisible = currentScreen is PoppoScreen.Home || currentScreen is PoppoScreen.Messages || currentScreen is PoppoScreen.Profile

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = PoppoBackground,
        bottomBar = {
            if (isBottomNavVisible) {
                NavigationBar(
                    containerColor = PoppoSurface,
                    contentColor = Color.White,
                    tonalElevation = 8.dp,
                    windowInsets = WindowInsets.navigationBars,
                    modifier = Modifier.testTag("bottom_nav_bar")
                ) {
                    // Tab 1: Live
                    NavigationBarItem(
                        selected = currentScreen is PoppoScreen.Home && activeBottomTab == BottomNavTab.LIVE,
                        onClick = {
                            activeBottomTab = BottomNavTab.LIVE
                            viewModel.selectCategory(LiveCategory.POPULAR)
                            viewModel.navigateTo(PoppoScreen.Home)
                        },
                        icon = {
                            Icon(
                                imageVector = if (activeBottomTab == BottomNavTab.LIVE) Icons.Filled.Tv else Icons.Outlined.Tv,
                                contentDescription = "Live"
                            )
                        },
                        label = { Text("Live", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = PoppoPinkAccent,
                            indicatorColor = PoppoPinkAccent,
                            unselectedIconColor = PoppoTextMuted,
                            unselectedTextColor = PoppoTextMuted
                        ),
                        modifier = Modifier.testTag("nav_tab_live")
                    )

                    // Tab 2: Party
                    NavigationBarItem(
                        selected = currentScreen is PoppoScreen.Home && activeBottomTab == BottomNavTab.PARTY,
                        onClick = {
                            activeBottomTab = BottomNavTab.PARTY
                            viewModel.selectCategory(LiveCategory.PARTY)
                            viewModel.navigateTo(PoppoScreen.Home)
                        },
                        icon = {
                            Icon(
                                imageVector = if (activeBottomTab == BottomNavTab.PARTY) Icons.Filled.Mic else Icons.Outlined.Mic,
                                contentDescription = "Party"
                            )
                        },
                        label = { Text("Party", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = PoppoPinkAccent,
                            indicatorColor = PoppoPinkAccent,
                            unselectedIconColor = PoppoTextMuted,
                            unselectedTextColor = PoppoTextMuted
                        ),
                        modifier = Modifier.testTag("nav_tab_party")
                    )

                    // Tab 3: Messages
                    NavigationBarItem(
                        selected = currentScreen is PoppoScreen.Messages,
                        onClick = {
                            activeBottomTab = BottomNavTab.MESSAGES
                            viewModel.navigateTo(PoppoScreen.Messages)
                        },
                        icon = {
                            BadgedBox(badge = {
                                Badge(containerColor = PoppoPinkAccent) {
                                    Text("1", fontSize = 9.sp, color = Color.White)
                                }
                            }) {
                                Icon(
                                    imageVector = if (currentScreen is PoppoScreen.Messages) Icons.Filled.ChatBubble else Icons.Outlined.ChatBubbleOutline,
                                    contentDescription = "Messages"
                                )
                            }
                        },
                        label = { Text("Messages", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = PoppoPinkAccent,
                            indicatorColor = PoppoPinkAccent,
                            unselectedIconColor = PoppoTextMuted,
                            unselectedTextColor = PoppoTextMuted
                        ),
                        modifier = Modifier.testTag("nav_tab_messages")
                    )

                    // Tab 4: Profile
                    NavigationBarItem(
                        selected = currentScreen is PoppoScreen.Profile,
                        onClick = {
                            activeBottomTab = BottomNavTab.PROFILE
                            viewModel.navigateTo(PoppoScreen.Profile)
                        },
                        icon = {
                            Icon(
                                imageVector = if (currentScreen is PoppoScreen.Profile) Icons.Filled.Person else Icons.Outlined.Person,
                                contentDescription = "Profile"
                            )
                        },
                        label = { Text("Me", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = PoppoPinkAccent,
                            indicatorColor = PoppoPinkAccent,
                            unselectedIconColor = PoppoTextMuted,
                            unselectedTextColor = PoppoTextMuted
                        ),
                        modifier = Modifier.testTag("nav_tab_profile")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val screen = currentScreen) {
                is PoppoScreen.Home -> HomeScreen(viewModel = viewModel)
                is PoppoScreen.LiveRoom -> LiveRoomScreen(streamId = screen.streamId, viewModel = viewModel)
                is PoppoScreen.PartyRoom -> PartyRoomScreen(streamId = screen.streamId, viewModel = viewModel)
                is PoppoScreen.GoLive -> GoLiveScreen(viewModel = viewModel)
                is PoppoScreen.Messages -> MessagesScreen(viewModel = viewModel)
                is PoppoScreen.DirectChat -> DirectChatScreen(
                    hostId = screen.hostId,
                    hostName = screen.hostName,
                    hostAvatar = screen.hostAvatar,
                    viewModel = viewModel
                )
                is PoppoScreen.Profile -> ProfileScreen(viewModel = viewModel)
            }
        }
    }

    // Global Dialogs
    if (showRecharge) {
        RechargeDialog(
            userCoins = userProfile.coins,
            onRechargeSelected = { amount ->
                viewModel.rechargeCoins(amount)
            },
            onDismiss = { viewModel.closeRecharge() }
        )
    }

    if (showLuckyWheel) {
        LuckyWheelDialog(
            freeSpins = userProfile.freeWheelSpins,
            onSpinClaimed = { viewModel.spinLuckyWheel() },
            onDismiss = { viewModel.closeLuckyWheel() }
        )
    }

    if (showDailyTask) {
        DailyTaskDialog(
            tasks = dailyTasks,
            checkedInToday = userProfile.checkedInToday,
            streak = userProfile.checkInStreak,
            onClaimCheckIn = { viewModel.claimDailyCheckIn() },
            onClaimTask = { taskId -> viewModel.claimTask(taskId) },
            onDismiss = { viewModel.closeDailyTask() }
        )
    }
}
