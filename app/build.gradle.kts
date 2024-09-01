plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.compose.compiler)
    id("com.android.application")
    id("com.google.devtools.ksp")
    id("kotlin-kapt") // untuk data binding di xml
    id("androidx.navigation.safeargs.kotlin")
    id("org.jetbrains.kotlin.plugin.parcelize")
    id("com.google.dagger.hilt.android")
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

android {
    namespace = "com.example.gratefulnote"

    compileSdk = 34

    testBuildType = "debugTest"

    defaultConfig {
        applicationId = "com.hezapp.gratefulnote"
        testApplicationId = "com.hezapp.gratefulnote_instrumentation"
        minSdk = 23
        targetSdk = 34
        versionCode = 2
        versionName = "2.0"

        // testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunner = "com.example.gratefulnote.runner.HiltTestRunner"

//        javaCompileOptions {
//            annotationProcessorOptions {
//                arguments["room.schemaLocation"] = "$projectDir/schemas"
//            }
//        }
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
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

    }

    buildFeatures {
        compose = true
        buildConfig = true
        dataBinding = true
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8

    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    sourceSets {
        // Adds exported schema location as test app assets.
        getByName("androidTest").assets.srcDirs("$projectDir/schemas")
    }
}

dependencies {
    implementation("androidx.test.ext:junit-ktx:1.1.3")
    implementation("androidx.test.espresso:espresso-contrib:3.5.1")

    // room version
    annotationProcessor(libs.room.compiler)
    ksp(libs.room.compiler)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    testImplementation(libs.room.testing)

    // nav version
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.1")

    testImplementation("junit:junit:4.13.2")


    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.test:runner:1.4.0")


    // Recycler view
    implementation("androidx.recyclerview:recyclerview:1.2.1")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")

    // fragment
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    implementation("com.google.code.gson:gson:2.9.1")


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
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.1")
    androidTestImplementation("androidx.test.uiautomator:uiautomator:2.3.0")


    androidTestImplementation("androidx.activity:activity-compose")

    // dagger-hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation)
    ksp(libs.hilt.compiler)
    androidTestImplementation(libs.hilt.android.test)
    kspAndroidTest(libs.hilt.android.compiler)

    androidTestImplementation("org.mockito:mockito-core:5.11.0")
    androidTestImplementation("org.mockito:mockito-android:5.11.0")
}