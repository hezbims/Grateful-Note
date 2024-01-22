package com.example.gratefulnote.debug.test

import io.cucumber.android.runner.CucumberAndroidJUnitRunner
import io.cucumber.junit.CucumberOptions

@CucumberOptions(
    glue = [ "com.example.gratefulnote.steps_definition" ],
    features = [ "feature_files" ]
)
class CucumberRunner : CucumberAndroidJUnitRunner()