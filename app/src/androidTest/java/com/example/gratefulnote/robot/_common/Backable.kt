package com.example.gratefulnote.robot._common

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice

abstract class Backable {
    fun pressBack(){
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).pressBack()
    }
}