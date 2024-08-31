// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    id("androidx.navigation.safeargs") version "2.5.2" apply false
    id("com.android.application") version "8.1.4" apply false
    id("com.android.library") version "8.1.4" apply false
    id("org.jetbrains.kotlin.android") version "1.9.20" apply false
    id("com.google.dagger.hilt.android") version "2.50" apply false
}

tasks.register("clean", Delete::class){
    this.delete(rootProject.buildDir)
}