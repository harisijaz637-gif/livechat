package com.example.data.model

import com.example.R

enum class LiveCategory(val label: String) {
    POPULAR("Popular"),
    PK("PK Battle"),
    PARTY("Party Rooms"),
    TALENT("Talent & Music"),
    FOLLOWED("Followed")
}

data class PkOpponent(
    val id: String,
    val name: String,
    val avatar: String,
    val countryFlag: String,
    val fansCount: String
)

data class PartySeat(
    val seatIndex: Int,
    val userName: String? = null,
    val userAvatar: String? = null,
    val isHost: Boolean = false,
    val isSpeaking: Boolean = false,
    val isMuted: Boolean = false,
    val isLocked: Boolean = false,
    val coinsSent: Int = 0
)

data class LiveStream(
    val id: String,
    val hostId: String,
    val hostName: String,
    val hostAvatar: String,
    val countryFlag: String,
    val title: String,
    val category: LiveCategory,
    val tags: List<String>,
    val viewerCount: Int,
    val diamondsEarned: Long,
    val isPkBattle: Boolean = false,
    val pkOpponent: PkOpponent? = null,
    val pkScoreHost: Int = 0,
    val pkScoreOpponent: Int = 0,
    val pkTimeRemainingSec: Int = 180,
    val isPartyRoom: Boolean = false,
    val partySeats: List<PartySeat> = emptyList(),
    val hotRank: Int = 1,
    val isFollowed: Boolean = false,
    val bio: String = "Welcome to my live stream! Send gifts & join the party 🎉",
    val primaryColorHex: Long = 0xFF8C3BFF,
    val secondaryColorHex: Long = 0xFFFF2D85
)

data class LiveMessage(
    val id: String,
    val senderName: String,
    val senderAvatar: String,
    val senderLevel: Int = 5,
    val vipLevel: Int = 0,
    val text: String,
    val isGift: Boolean = false,
    val giftName: String? = null,
    val giftResId: Int? = null,
    val giftCount: Int = 1,
    val isSystem: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

enum class GiftAnimType {
    ROSE_BURST,
    HEART_SHOWER,
    TEDDY_BOUNCE,
    SUPERCAR_RUSH,
    DRAGON_FIREWORKS
}

data class GiftItem(
    val id: String,
    val name: String,
    val coins: Int,
    val drawableRes: Int? = null,
    val emoji: String,
    val animType: GiftAnimType,
    val description: String
)

data class UserProfile(
    val poppoId: String = "8392014",
    val name: String = "VIP Malik",
    val avatar: String = "👑",
    val vipLevel: Int = 3,
    val wealthLevel: Int = 18,
    val charmLevel: Int = 12,
    val coins: Long = 2500L,
    val diamonds: Long = 480L,
    val followingCount: Int = 14,
    val followersCount: Int = 380,
    val bio: String = "Poppo King 👑 | Live Stream Enthusiast | Big Gifter ✨",
    val checkedInToday: Boolean = false,
    val checkInStreak: Int = 3,
    val freeWheelSpins: Int = 2
)

data class DailyTask(
    val id: String,
    val title: String,
    val rewardCoins: Int,
    val currentProgress: Int,
    val maxProgress: Int,
    val isClaimed: Boolean = false
)

data class DirectMessage(
    val id: String,
    val hostId: String,
    val hostName: String,
    val hostAvatar: String,
    val countryFlag: String,
    val lastMessage: String,
    val timeAgo: String,
    val unreadCount: Int = 0,
    val isOnline: Boolean = true
)

data class ChatMessage(
    val id: String,
    val text: String,
    val isMe: Boolean,
    val timeStr: String,
    val isAudio: Boolean = false
)
