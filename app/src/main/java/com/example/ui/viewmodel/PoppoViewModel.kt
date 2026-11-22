package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.PoppoRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

sealed class PoppoScreen {
    object Home : PoppoScreen()
    data class LiveRoom(val streamId: String) : PoppoScreen()
    data class PartyRoom(val streamId: String) : PoppoScreen()
    object GoLive : PoppoScreen()
    object Messages : PoppoScreen()
    data class DirectChat(val hostId: String, val hostName: String, val hostAvatar: String) : PoppoScreen()
    object Profile : PoppoScreen()
}

data class FloatingHeart(
    val id: Long,
    val startXFraction: Float,
    val colorHex: Long,
    val sizeDp: Float
)

class PoppoViewModel(
    val repository: PoppoRepository = PoppoRepository()
) : ViewModel() {

    private val _currentScreen = MutableStateFlow<PoppoScreen>(PoppoScreen.Home)
    val currentScreen: StateFlow<PoppoScreen> = _currentScreen.asStateFlow()

    private val _selectedCategory = MutableStateFlow(LiveCategory.POPULAR)
    val selectedCategory: StateFlow<LiveCategory> = _selectedCategory.asStateFlow()

    // Dialog states
    private val _showRechargeDialog = MutableStateFlow(false)
    val showRechargeDialog: StateFlow<Boolean> = _showRechargeDialog.asStateFlow()

    private val _showLuckyWheelDialog = MutableStateFlow(false)
    val showLuckyWheelDialog: StateFlow<Boolean> = _showLuckyWheelDialog.asStateFlow()

    private val _showDailyTaskDialog = MutableStateFlow(false)
    val showDailyTaskDialog: StateFlow<Boolean> = _showDailyTaskDialog.asStateFlow()

    private val _showGiftBottomSheet = MutableStateFlow(false)
    val showGiftBottomSheet: StateFlow<Boolean> = _showGiftBottomSheet.asStateFlow()

    private val _floatingHearts = MutableStateFlow<List<FloatingHeart>>(emptyList())
    val floatingHearts: StateFlow<List<FloatingHeart>> = _floatingHearts.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    // Host broadcast stats when User is broadcasting via "Go Live"
    val isUserBroadcasting = MutableStateFlow(false)
    val broadcastViewerCount = MutableStateFlow(1)
    val broadcastDiamonds = MutableStateFlow(0L)
    val broadcastDurationSeconds = MutableStateFlow(0)
    private var broadcastJob: Job? = null

    private var roomSimulationJob: Job? = null

    init {
        // Observe streams and user profile from repository
    }

    fun selectCategory(cat: LiveCategory) {
        _selectedCategory.value = cat
    }

    fun navigateTo(screen: PoppoScreen) {
        _currentScreen.value = screen
        if (screen is PoppoScreen.LiveRoom) {
            val stream = repository.streams.value.find { it.id == screen.streamId }
            if (stream != null) {
                repository.initRoomMessages(stream)
                startRoomSimulation(stream.id)
            }
        } else if (screen is PoppoScreen.PartyRoom) {
            val stream = repository.streams.value.find { it.id == screen.streamId }
            if (stream != null) {
                repository.initRoomMessages(stream)
                startRoomSimulation(stream.id)
            }
        } else {
            stopRoomSimulation()
        }
    }

    fun navigateBack() {
        stopRoomSimulation()
        _currentScreen.value = PoppoScreen.Home
    }

    fun openRecharge() { _showRechargeDialog.value = true }
    fun closeRecharge() { _showRechargeDialog.value = false }

    fun openLuckyWheel() { _showLuckyWheelDialog.value = true }
    fun closeLuckyWheel() { _showLuckyWheelDialog.value = false }

    fun openDailyTask() { _showDailyTaskDialog.value = true }
    fun closeDailyTask() { _showDailyTaskDialog.value = false }

    fun openGiftSheet() { _showGiftBottomSheet.value = true }
    fun closeGiftSheet() { _showGiftBottomSheet.value = false }

    fun clearToast() { _toastMessage.value = null }

    fun showToast(msg: String) {
        _toastMessage.value = msg
    }

    fun addFloatingHeart() {
        val colors = listOf(0xFFFF2D85, 0xFF8C3BFF, 0xFFFFB300, 0xFF00E5FF, 0xFFFF1744)
        val heart = FloatingHeart(
            id = System.currentTimeMillis() + Random.nextLong(1000),
            startXFraction = Random.nextFloat() * 0.4f + 0.5f,
            colorHex = colors.random(),
            sizeDp = Random.nextInt(24, 38).toFloat()
        )
        _floatingHearts.update { (it + heart).takeLast(15) }
        viewModelScope.launch {
            delay(1800)
            _floatingHearts.update { list -> list.filter { it.id != heart.id } }
        }
    }

    fun sendGift(streamId: String, gift: GiftItem, count: Int = 1) {
        val success = repository.sendGift(streamId, gift, count)
        if (success) {
            showToast("Sent ${gift.name} x$count! 🎉")
            addFloatingHeart()
        } else {
            showToast("Insufficient Coins! Please recharge.")
            openRecharge()
        }
    }

    fun toggleFollow(streamId: String) {
        repository.toggleFollow(streamId)
        val stream = repository.streams.value.find { it.id == streamId }
        val msg = if (stream?.isFollowed == true) "Followed host! ❤️" else "Unfollowed"
        showToast(msg)
    }

    fun claimDailyCheckIn() {
        val bonus = repository.claimDailyCheckIn()
        if (bonus > 0) {
            showToast("Claimed +$bonus Gold Coins! 🌟")
        } else {
            showToast("Already checked in today!")
        }
    }

    fun spinLuckyWheel() {
        val prize = repository.spinLuckyWheel()
        if (prize > 0) {
            showToast("Jackpot! Won +$prize Gold Coins! 🎰")
        } else {
            showToast("No free spins left today!")
        }
    }

    fun rechargeCoins(amount: Long) {
        repository.rechargeCoins(amount)
        closeRecharge()
        showToast("Success! +$amount Coins added to your wallet! 💎")
    }

    fun claimTask(taskId: String) {
        val reward = repository.claimTask(taskId)
        if (reward > 0) {
            showToast("Task completed! +$reward Coins! 🎁")
        }
    }

    fun togglePartySeat(streamId: String, seatIndex: Int) {
        repository.togglePartySeat(streamId, seatIndex)
    }

    fun sendLiveComment(text: String) {
        if (text.isNotBlank()) {
            repository.addLiveComment(text)
        }
    }

    fun sendDirectChat(hostId: String, text: String) {
        if (text.isNotBlank()) {
            repository.sendDirectChatMessage(hostId, text)
            // Simulated host reply after 1.5s
            viewModelScope.launch {
                delay(1500)
                val replies = listOf(
                    "Hey! Thanks for watching my stream 💖",
                    "Aww you're so sweet! Join my party room next time!",
                    "Hope you have a fantastic day! ✨",
                    "Thanks for the support in the PK battle! 👑"
                )
                repository.sendDirectChatMessage(hostId, replies.random())
            }
        }
    }

    private fun startRoomSimulation(streamId: String) {
        stopRoomSimulation()
        roomSimulationJob = viewModelScope.launch {
            while (true) {
                delay(Random.nextLong(2500, 5000))
                repository.addSimulatedViewerComment()
            }
        }
    }

    private fun stopRoomSimulation() {
        roomSimulationJob?.cancel()
        roomSimulationJob = null
    }

    // Host's Go Live Controls
    fun startUserBroadcast(title: String, category: String) {
        isUserBroadcasting.value = true
        broadcastViewerCount.value = 14
        broadcastDiamonds.value = 0L
        broadcastDurationSeconds.value = 0

        broadcastJob?.cancel()
        broadcastJob = viewModelScope.launch {
            while (isUserBroadcasting.value) {
                delay(1000)
                broadcastDurationSeconds.value += 1
                if (broadcastDurationSeconds.value % 4 == 0) {
                    broadcastViewerCount.value += Random.nextInt(1, 5)
                }
                if (broadcastDurationSeconds.value % 8 == 0) {
                    val giftsEarned = listOf(10, 50, 199).random()
                    broadcastDiamonds.value += giftsEarned
                    repository.addSimulatedViewerComment()
                }
            }
        }
    }

    fun stopUserBroadcast() {
        isUserBroadcasting.value = false
        broadcastJob?.cancel()
        broadcastJob = null
    }

    override fun onCleared() {
        super.onCleared()
        stopRoomSimulation()
        stopUserBroadcast()
    }
}
