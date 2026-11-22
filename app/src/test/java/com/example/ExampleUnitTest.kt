package com.example

import com.example.data.repository.PoppoRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testPoppoRepositoryInitialStateAndGift() {
    val repository = PoppoRepository()
    assertTrue(repository.streams.value.isNotEmpty())
    val initialCoins = repository.userProfile.value.coins
    val gift = repository.giftCatalog.first()
    val success = repository.sendGift("stream_1", gift, 1)
    assertTrue(success)
    assertEquals(initialCoins - gift.coins, repository.userProfile.value.coins)
  }

  @Test
  fun testDailyCheckInBonus() {
    val repository = PoppoRepository()
    val bonus = repository.claimDailyCheckIn()
    assertTrue(bonus > 0)
    assertTrue(repository.userProfile.value.checkedInToday)
  }
}
