package com.example.gratefulnote.utils

import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

/**
 * Menunggu sampai body dari function ini selesai
 * @param maxIteration jumlah pengulangan maksimal, setiap pengulangan memiliki jeda selama 250 milliseconds
 */
suspend fun waitUntil(
    maxIteration : Int = 5,
    body : () -> Boolean,
) {
    var currentIteration = 1
    while (true) {
        val isSucceed = body()
        if (isSucceed)
            break
        if (currentIteration++ == maxIteration)
            throw RuntimeException("Terlalu lama menunggu")
        delay(250.milliseconds)
    }
}