package com.example.gratefulnote.robot._common

import androidx.test.espresso.ViewInteraction

interface IEspressoComponent : IUiComponent {
    val viewInteractor : ViewInteraction
}