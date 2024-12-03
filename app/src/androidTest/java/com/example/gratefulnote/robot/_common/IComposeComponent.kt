package com.example.gratefulnote.robot._common

import androidx.compose.ui.test.SemanticsNodeInteraction

interface IComposeComponent : IUiComponent {
    val componentInteractor : SemanticsNodeInteraction
}