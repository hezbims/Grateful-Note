package com.example.gratefulnote.robot._common.node_interaction

import androidx.compose.ui.test.SemanticsMatcher

/**
 * Terkadang di semantic tree pada lazy list terdapat node yang sebenarnya tidak ada
 * karena hasil cache. Gunakan isNotCached untuk menghilangkan ghost node ini
 */
fun isNotCached() = SemanticsMatcher("isNotCached") {
    it.layoutInfo.isPlaced
}