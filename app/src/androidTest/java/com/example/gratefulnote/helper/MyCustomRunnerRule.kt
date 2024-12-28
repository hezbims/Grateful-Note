package com.example.gratefulnote.helper

import kotlinx.coroutines.runBlocking
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class MyCustomRunnerRule(
    private val tearDown: (suspend () -> Unit)? = null,
    private val action: suspend () -> Unit,
) : TestRule {
    override fun apply(base: Statement?, description: Description?): Statement {
        return object : Statement() {
            override fun evaluate() {
                try {
                    runBlocking {
                        action()
                    }
                    base?.evaluate()
                } finally {
                    runBlocking {
                        tearDown?.let { it() }
                    }
                }
            }
        }
    }
}