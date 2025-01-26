package com.example.gratefulnote.debug.test

import android.app.Application
import android.content.Context
import com.example.gratefulnote.runner.CustomTestApplication_Application
import io.cucumber.android.runner.CucumberAndroidJUnitRunner
import io.cucumber.junit.CucumberOptions

@CucumberOptions(
    glue = ["com.example.gratefulnote.steps"],
    features = ["feature_files"]
)
class MyCucumberRunner : CucumberAndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, CustomTestApplication_Application::class.java.name, context)
    }
}