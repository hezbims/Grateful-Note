package com.example.gratefulnote.common.domain

import android.content.BroadcastReceiver
import android.content.BroadcastReceiver.PendingResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun BroadcastReceiver.receiverGoAsync(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> Unit
) {
    val pendingResult : PendingResult? = goAsync()
    @OptIn(DelicateCoroutinesApi::class) // Must run globally; there's no teardown callback.
    GlobalScope.launch(context) {
        try {
            block()
        } finally {
            pendingResult?.finish()
        }
    }
}