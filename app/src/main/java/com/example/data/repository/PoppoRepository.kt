package com.example.data.repository

import com.example.R
import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class PoppoRepository {

    val giftCatalog: List<GiftItem> = listOf(
        GiftItem("gift_rose", "Rose", 1, null, "🌹", GiftAnimType.ROSE_BURST, "Sweet love rose"),
        GiftItem("gift_heart", "Neon Heart", 10, null, "💖", GiftAnimType.HEART_SHOWER, "Flashing glowing heart"),
        GiftItem("gift_mic", "Golden Mic", 50, null, "🎤", GiftAnimType.TEDDY_BOUNCE, "Singer's tribute"),
        GiftItem("gift_teddy", "Poppo Bear", 199, null, "🧸", GiftAnimType.TEDDY_BOUNCE, "Cute huggable teddy"),
        GiftItem("gift_supercar", "Supercar", 999, R.drawable.img_gift_supercar, "🏎️", GiftAnimType.SUPERCAR_RUSH, "Luxury neon speed machine"),
        GiftItem("gift_dragon", "Golden Dragon", 4999, R.drawable.img_gift_dragon, "🐉", GiftAnimType.DRAGON_FIREWORKS, "Supreme mythic golden dragon")
    )

    private val initialStreams = listOf(
        LiveStream(
            id = "stream_1",
            hostId = "host_zara",
            hostName = "Zara Khan",
            hostAvatar = "👑",
            countryFlag = "🇵🇰",
            title = "🌟 Late Night Songs & Acoustic Vibes | PK Battle!",
            category = LiveCategory.PK,
            tags = listOf("Singer", "PK Battle", "Urdu/Hindi", "Top 1"),
            viewerCount = 4820,
            diamondsEarned = 145000,
            isPkBattle = true,
            pkOpponent = PkOpponent("opp_ayla", "Ayla Rose", "🌹", "🇹🇷", "450K"),
            pkScoreHost = 2450,
            pkScoreOpponent = 2100,
            pkTimeRemainingSec = 165,
            hotRank = 1,
            isFollowed = false,
            bio = "Singer & Host | PK Queen 👑 | Thank you to all my gifters!",
            primaryColorHex = 0xFF8C3BFF,
            secondaryColorHex = 0xFFFF2D85
        ),
        LiveStream(
            id = "stream_2",
            hostId = "host_mike",
            hostName = "DJ Mike & Crew",
            hostAvatar = "🎧",
            countryFlag = "🇺🇸",
            title = "🎉 9-Seat Party Voice Club | Free Mic & Karaoke",
            category = LiveCategory.PARTY,
            tags = listOf("Voice Party", "Karaoke", "Chill", "Games"),
            viewerCount = 3120,
            diamondsEarned = 98400,
            isPartyRoom = true,
            partySeats = listOf(
                PartySeat(0, "DJ Mike", "🎧", isHost = true, isSpeaking = true, coinsSent = 12000),
                PartySeat(1, "Sara_VIP", "💎", isSpeaking = false, coinsSent = 8400),
                PartySeat(2, "Hamza_99", "⚡", isSpeaking = true, coinsSent = 3200),
                PartySeat(3, "Elena", "🌸", isSpeaking = false, coinsSent = 4500),
                PartySeat(4, null, null),
                PartySeat(5, "Kenji", "🔥", isSpeaking = false, coinsSent = 1100),
                PartySeat(6, null, null),
                PartySeat(7, "Nadia", "✨", isSpeaking = true, coinsSent = 5600),
                PartySeat(8, null, null)
            ),
            hotRank = 2,
            isFollowed = true,
            bio = "Official Poppo Party Room #102 | Hop on the mic and sing!",
            primaryColorHex = 0xFF00E5FF,
            secondaryColorHex = 0xFF6C14D0
        ),
        LiveStream(
            id = "stream_3",
            hostId = "host_noor",
            hostName = "Princess Noor",
            hostAvatar = "💎",
            countryFlag = "🇦🇪",
            title = "👑 VIP Crown Night | Top Gifter Wins Exclusive Badge!",
            category = LiveCategory.POPULAR,
            tags = listOf("Luxury", "VIP Party", "Top Gifter", "Arabic/Eng"),
            viewerCount = 6840,
            diamondsEarned = 320000,
            hotRank = 3,
            isFollowed = false,
            bio = "Luxury lifestyle, music & talk | Gold VIP Room",
            primaryColorHex = 0xFFFFB300,
            secondaryColorHex = 0xFFFF2D85
        ),
        LiveStream(
            id = "stream_4",
            hostId = "host_siti",
            hostName = "Siti Rahma",
            hostAvatar = "💃",
            countryFlag = "🇮🇩",
            title = "💃 Asian Dance Fusion & Live Music",
            category = LiveCategory.TALENT,
            tags = listOf("Dance", "Music", "Talent", "Energetic"),
            viewerCount = 2950,
            diamondsEarned = 72000,
            hotRank = 4,
            isFollowed = false,
            bio = "Professional dancer | Spreading good vibes only 🌸",
            primaryColorHex = 0xFFFF2D85,
            secondaryColorHex = 0xFF8C3BFF
        ),
        LiveStream(
            id = "stream_5",
            hostId = "host_hamza",
            hostName = "Hamza Rockstar",
            hostAvatar = "🎸",
            countryFlag = "🇵🇰",
            title = "🎸 Acoustic Concert & Requests | Coke Studio Hits",
            category = LiveCategory.TALENT,
            tags = listOf("Guitar", "Acoustic", "Live Singing", "Covers"),
            viewerCount = 2140,
            diamondsEarned = 54000,
            hotRank = 5,
            isFollowed = true,
            bio = "Guitarist and composer. Drop song requests in comments!",
            primaryColorHex = 0xFF00E676,
            secondaryColorHex = 0xFF160F2E
        ),
        LiveStream(
            id = "stream_6",
            hostId = "host_bella",
            hostName = "Bella & Maya PK",
            hostAvatar = "🔥",
            countryFlag = "🇹🇷",
            title = "⚔️ Double Host Mega PK Clash! Loser eats chili 🌶️",
            category = LiveCategory.PK,
            tags = listOf("PK Battle", "Challenger", "Penalty Game"),
            viewerCount = 7430,
            diamondsEarned = 280000,
            isPkBattle = true,
            pkOpponent = PkOpponent("opp_maya", "Maya Star", "✨", "🇬🇧", "890K"),
            pkScoreHost = 4800,
            pkScoreOpponent = 5200,
            pkTimeRemainingSec = 110,
            hotRank = 6,
            isFollowed = false,
            bio = "High stakes PK battles every night! Join Team Bella!",
            primaryColorHex = 0xFFFF1744,
            secondaryColorHex = 0xFF8C3BFF
        ),
        LiveStream(
            id = "stream_7",
            hostId = "host_club",
            hostName = "Club Midnight VIP",
            hostAvatar = "🍸",
            countryFlag = "🇬🇧",
            title = "🎙️ Global Chit-Chat & Matchmaking Voice Club",
            category = LiveCategory.PARTY,
            tags = listOf("Matchmaking", "Global", "9-Seat", "Nightlife"),
            viewerCount = 2890,
            diamondsEarned = 63000,
            isPartyRoom = true,
            partySeats = listOf(
                PartySeat(0, "Club Host", "🍸", isHost = true, isSpeaking = true, coinsSent = 9500),
                PartySeat(1, "Liam_UK", "💂", isSpeaking = true, coinsSent = 4000),
                PartySeat(2, null, null),
                PartySeat(3, "Chloe", "💄", isSpeaking = false, coinsSent = 3200),
                PartySeat(4, null, null),
                PartySeat(5, null, null),
                PartySeat(6, "Tariq", "🦅", isSpeaking = true, coinsSent = 6100),
                PartySeat(7, null, null),
                PartySeat(8, null, null)
            ),
            hotRank = 7,
            isFollowed = false,
            bio = "Connect with friends from London, Dubai, New York & Lahore!",
            primaryColorHex = 0xFF8C3BFF,
            secondaryColorHex = 0xFF00E5FF
        )
    )

    private val _streams = MutableStateFlow(initialStreams)
    val streams: StateFlow<List<LiveStream>> = _streams.asStateFlow()

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _dailyTasks = MutableStateFlow(
        listOf(
            DailyTask("t1", "Watch Live Streams for 3 minutes", 50, 3, 3, false),
            DailyTask("t2", "Send any gift to a host", 100, 1, 1, false),
            DailyTask("t3", "Follow 2 new talented hosts", 80, 2, 2, false),
            DailyTask("t4", "Join a 9-Seat Party Voice Room", 60, 1, 1, false),
            DailyTask("t5", "Send 10 hearts in any Live stream", 40, 10, 10, false)
        )
    )
    val dailyTasks: StateFlow<List<DailyTask>> = _dailyTasks.asStateFlow()

    private val _directMessages = MutableStateFlow(
        listOf(
            DirectMessage("dm_1", "host_zara", "Zara Khan", "👑", "🇵🇰", "Thank you so much for the Supercar gift! You made my night ❤️", "2m ago", 1, true),
            DirectMessage("dm_2", "host_mike", "DJ Mike & Crew", "🎧", "🇺🇸", "Hey bro, the mic in seat #4 is open if you want to hop on!", "15m ago", 0, true),
            DirectMessage("dm_3", "host_noor", "Princess Noor", "💎", "🇦🇪", "Will see you in tomorrow's VIP Crown broadcast ✨", "1h ago", 0, false),
            DirectMessage("dm_sys", "sys_poppo", "Poppo Live Team", "📢", "🌐", "Congratulations! You reached Wealth Level 18 badge!", "3h ago", 0, true)
        )
    )
    val directMessages: StateFlow<List<DirectMessage>> = _directMessages.asStateFlow()

    private val _chatMessagesMap = MutableStateFlow<Map<String, List<ChatMessage>>>(
        mapOf(
            "host_zara" to listOf(
                ChatMessage("m1", "Hey Zara! Amazing singing today!", true, "10:30 PM"),
                ChatMessage("m2", "Thank you so much! What song would you like to hear?", false, "10:31 PM"),
                ChatMessage("m3", "Can you sing your favorite acoustic track?", true, "10:32 PM"),
                ChatMessage("m4", "Thank you so much for the Supercar gift! You made my night ❤️", false, "10:35 PM")
            ),
            "host_mike" to listOf(
                ChatMessage("m1", "Love the beats in the party room DJ Mike!", true, "9:15 PM"),
                ChatMessage("m2", "Hey bro, the mic in seat #4 is open if you want to hop on!", false, "9:20 PM")
            )
        )
    )
    val chatMessagesMap: StateFlow<Map<String, List<ChatMessage>>> = _chatMessagesMap.asStateFlow()

    // Real-time messages in the currently watched live room
    private val _liveRoomMessages = MutableStateFlow<List<LiveMessage>>(emptyList())
    val liveRoomMessages: StateFlow<List<LiveMessage>> = _liveRoomMessages.asStateFlow()

    // Active Gift Animation on screen
    private val _activeGiftAnimation = MutableStateFlow<GiftItem?>(null)
    val activeGiftAnimation: StateFlow<GiftItem?> = _activeGiftAnimation.asStateFlow()

    fun dismissGiftAnimation() {
        _activeGiftAnimation.value = null
    }

    fun initRoomMessages(stream: LiveStream) {
        val seed = listOf(
            LiveMessage("m_sys", "Poppo System", "🛡️", 99, 7, "Welcome to ${stream.hostName}'s live! Be respectful and follow community guidelines.", isSystem = true),
            LiveMessage("m_1", "Sultan_KSA", "👑", 24, 5, "Let's go team!! Sending full support 🌟"),
            LiveMessage("m_2", "Amina_Lahore", "🌸", 8, 0, "So pretty and talented! ❤️"),
            LiveMessage("m_3", "Tariq_Dubai", "💎", 15, 2, "Drop the big gifts guys! PK is close!")
        )
        _liveRoomMessages.value = seed
    }

    fun addLiveComment(text: String) {
        val user = _userProfile.value
        val newMsg = LiveMessage(
            id = "msg_${System.currentTimeMillis()}",
            senderName = user.name,
            senderAvatar = user.avatar,
            senderLevel = user.wealthLevel,
            vipLevel = user.vipLevel,
            text = text
        )
        _liveRoomMessages.update { listOf(newMsg) + it.take(40) }
    }

    fun addSimulatedViewerComment() {
        val sampleNames = listOf("Ali_99", "Sara_Karachi", "Leo_NY", "Fatima_Isb", "Bilal_Rox", "Zainab_UK", "Hassan_PK", "Noor_Star")
        val sampleAvatars = listOf("⭐", "🔥", "🌹", "💎", "⚡", "✨", "🦋", "🦁")
        val sampleTexts = listOf(
            "Mashallah great live! 💖",
            "Send more hearts tap tap tap!",
            "PK score is insane right now 🔥",
            "Who sent the Supercar?! Wow!",
            "Love from Pakistan 🇵🇰",
            "Sing one more song please! 🎶",
            "Team Host all the way! 👑",
            "Drop the Dragon combo! 🐉"
        )
        val name = sampleNames.random()
        val avatar = sampleAvatars.random()
        val text = sampleTexts.random()

        val msg = LiveMessage(
            id = "sim_${System.currentTimeMillis()}_${Random.nextInt(1000)}",
            senderName = name,
            senderAvatar = avatar,
            senderLevel = Random.nextInt(3, 20),
            vipLevel = if (Random.nextBoolean()) Random.nextInt(1, 4) else 0,
            text = text
        )
        _liveRoomMessages.update { listOf(msg) + it.take(40) }
    }

    fun sendGift(streamId: String, gift: GiftItem, count: Int = 1): Boolean {
        val totalCost = gift.coins * count
        val currentCoins = _userProfile.value.coins
        if (currentCoins < totalCost) {
            return false
        }

        // Deduct coins & increase host diamonds
        _userProfile.update { it.copy(coins = it.coins - totalCost) }
        _streams.update { list ->
            list.map { stream ->
                if (stream.id == streamId) {
                    val newDiamonds = stream.diamondsEarned + totalCost
                    val newScore = if (stream.isPkBattle) stream.pkScoreHost + totalCost else stream.pkScoreHost
                    stream.copy(diamondsEarned = newDiamonds, pkScoreHost = newScore)
                } else stream
            }
        }

        // Add gift message
        val user = _userProfile.value
        val giftMsg = LiveMessage(
            id = "gift_${System.currentTimeMillis()}",
            senderName = user.name,
            senderAvatar = user.avatar,
            senderLevel = user.wealthLevel,
            vipLevel = user.vipLevel,
            text = "sent ${gift.name} x$count ${gift.emoji}",
            isGift = true,
            giftName = gift.name,
            giftResId = gift.drawableRes,
            giftCount = count
        )
        _liveRoomMessages.update { listOf(giftMsg) + it.take(40) }

        // Trigger animation
        _activeGiftAnimation.value = gift
        return true
    }

    fun toggleFollow(streamId: String) {
        var followedDelta = 0
        _streams.update { list ->
            list.map { stream ->
                if (stream.id == streamId) {
                    val newFollowed = !stream.isFollowed
                    followedDelta = if (newFollowed) 1 else -1
                    stream.copy(isFollowed = newFollowed)
                } else stream
            }
        }
        _userProfile.update {
            it.copy(followingCount = (it.followingCount + followedDelta).coerceAtLeast(0))
        }
    }

    fun togglePartySeat(streamId: String, seatIndex: Int) {
        val user = _userProfile.value
        _streams.update { list ->
            list.map { stream ->
                if (stream.id == streamId && stream.isPartyRoom) {
                    val updatedSeats = stream.partySeats.toMutableList()
                    val currentSeat = updatedSeats.getOrNull(seatIndex)
                    if (currentSeat != null) {
                        if (currentSeat.userName == user.name) {
                            // Leave seat
                            updatedSeats[seatIndex] = PartySeat(seatIndex, null, null)
                        } else if (currentSeat.userName == null) {
                            // Take seat
                            updatedSeats[seatIndex] = PartySeat(
                                seatIndex = seatIndex,
                                userName = user.name,
                                userAvatar = user.avatar,
                                isSpeaking = true,
                                isMuted = false
                            )
                        }
                    }
                    stream.copy(partySeats = updatedSeats)
                } else stream
            }
        }
    }

    fun claimDailyCheckIn(): Int {
        val user = _userProfile.value
        if (user.checkedInToday) return 0
        val bonus = 200 + (user.checkInStreak * 50)
        _userProfile.update {
            it.copy(
                coins = it.coins + bonus,
                checkedInToday = true,
                checkInStreak = it.checkInStreak + 1
            )
        }
        return bonus
    }

    fun spinLuckyWheel(): Int {
        val user = _userProfile.value
        if (user.freeWheelSpins <= 0) return 0
        val rewards = listOf(50, 100, 200, 500, 1000, 2500)
        val prize = rewards.random()
        _userProfile.update {
            it.copy(
                coins = it.coins + prize,
                freeWheelSpins = it.freeWheelSpins - 1
            )
        }
        return prize
    }

    fun claimTask(taskId: String): Int {
        var reward = 0
        _dailyTasks.update { list ->
            list.map { task ->
                if (task.id == taskId && !task.isClaimed && task.currentProgress >= task.maxProgress) {
                    reward = task.rewardCoins
                    task.copy(isClaimed = true)
                } else task
            }
        }
        if (reward > 0) {
            _userProfile.update { it.copy(coins = it.coins + reward) }
        }
        return reward
    }

    fun rechargeCoins(amount: Long) {
        _userProfile.update { it.copy(coins = it.coins + amount) }
    }

    fun sendDirectChatMessage(hostId: String, text: String) {
        val newChat = ChatMessage(
            id = "chat_${System.currentTimeMillis()}",
            text = text,
            isMe = true,
            timeStr = "Just now"
        )
        val currentList = _chatMessagesMap.value[hostId] ?: emptyList()
        val updated = currentList + newChat
        _chatMessagesMap.update { it + (hostId to updated) }

        // Update last message in DM list
        _directMessages.update { list ->
            list.map { dm ->
                if (dm.hostId == hostId) {
                    dm.copy(lastMessage = text, timeAgo = "Just now")
                } else dm
            }
        }
    }
}
