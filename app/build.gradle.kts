import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
    kotlin("kapt") // untuk data binding di xml
    alias(libs.plugins.navigation.safeargs)
    id("kotlin-parcelize")
    alias(libs.plugins.hilt.android)
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}



android {
    namespace = "com.example.gratefulnote"

    compileSdk = 34

    testBuildType = "debugTest"

    testOptions {
        animationsDisabled = true
    }

    defaultConfig {
        applicationId = "com.hezapp.gratefulnote"
        testApplicationId = "com.example.gratefulnote.debug.test"
        minSdk = 23
        targetSdk = 34
        versionCode = 2
        versionName = "2.0"

        // testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
//         testInstrumentationRunner = "com.example.gratefulnote.runner.HiltTestRunner"
        testInstrumentationRunner = "com.example.gratefulnote.debug.test.MyCucumberRunner"
    }

    signingConfigs {
        create("release"){
            val keystoreProperties = Properties()
            rootProject.file("key.properties").let {
                if (it.exists())
                    keystoreProperties.load(FileInputStream(it))
            }

            keyAlias = keystoreProperties.getProperty("keyAlias")
            keyPassword = keystoreProperties.getProperty("keyPassword")
            storeFile = file(keystoreProperties.getProperty("storeFile"))
            storePassword = keystoreProperties.getProperty("storePassword")
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
        register("debugTest") {
            initWith(getByName("debug"))
            applicationIdSuffix = ".test"
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            // isDebuggable = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            signingConfig = signingConfigs.getByName("release")
        }

    }

    buildFeatures {
        compose = true
        buildConfig = true
        dataBinding = true
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21

    }
    kotlinOptions {
        jvmTarget = "21"
    }

    sourceSets {
        // Adds exported schema location as test app assets.
        getByName("androidTest").assets.srcDirs("$projectDir/schemas")
    }
}

dependencies {
    // Room
    ksp(libs.room.compiler)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    testImplementation(libs.room.testing)

    // Navigation
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)

    // Espresso
    implementation(libs.espresso.contrib)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.espresso.intents)

    // Androidx.Test.Extension
    implementation(libs.junit.ktx)
    androidTestImplementation(libs.junit)

    // Androidx.Test
    androidTestImplementation(libs.test.runner)

    // Androidx.Core
    implementation(libs.core.ktx)

    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.1")

    testImplementation("junit:junit:4.13.2")


    // Recycler view
    implementation("androidx.recyclerview:recyclerview:1.2.1")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")

    // fragment
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    // GSON
    implementation("com.google.code.gson:gson:2.9.1")

    // Paging
    implementation(libs.paging.common)
    implementation(libs.paging.runtime.ktx)


    val composeBom = platform("androidx.compose:compose-bom:2023.10.01")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.activity:activity-compose")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose")

    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation("androidx.test.uiautomator:uiautomator:2.3.0")


    androidTestImplementation("androidx.activity:activity-compose")

    // dagger-hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation)
    ksp(libs.hilt.compiler)
    androidTestImplementation(libs.hilt.android.test)
    kspAndroidTest(libs.hilt.android.compiler)

    androidTestImplementation(libs.mockito.core)
    androidTestImplementation(libs.mockito.android)

    androidTestImplementation(libs.cucumber.android)
    androidTestImplementation(libs.cucumber.android.hilt)
//    androidTestImplementation(libs.cucumber.picocontainer)

    androidTestRuntimeOnly(libs.uiautomator)
}