package com.example.gratefulnote.utils

import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

/**
 * Menunggu sampai body dari function ini berhasil selesai tanpa melempar throwable
 * @param maxIteration jumlah pengulangan maksimal, setiap pengulangan memiliki jeda selama 250 milliseconds
 */
suspend fun waitUntilSucceed(
    maxIteration : Int = 5,
    tag: String? = null,
    body : () -> Unit,
) {
    var currentIteration = 1
    while (true) {
        try {
            body()
            break
        } catch (t : Throwable) {
            if (currentIteration++ == maxIteration)
                throw RuntimeException("Terlalu lama menunggu${ 
                    tag?.let { " : $it" } ?: ""
                }")
            delay(250.milliseconds)
        }
    }
}