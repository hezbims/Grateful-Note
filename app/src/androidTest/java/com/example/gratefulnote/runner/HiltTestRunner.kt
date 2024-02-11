package com.example.gratefulnote.runner

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class HiltTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        // Warning banget disini
        // aku ngestuck disini seharian cuma karena aku pake :
        // ::class::java.name
        // seharusnya :
        // ::class.java.name
        return super.newApplication(cl, CustomTestApplication_Application::class.java.name, context)
    }
}