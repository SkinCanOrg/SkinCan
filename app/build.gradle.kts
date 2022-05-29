import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

@Suppress("UnstableApiUsage")
android {
    compileSdk = Android.compileSdk

    defaultConfig {
        applicationId = Android.applicationId
        minSdk = Android.minSdk
        targetSdk = Android.targetSdk
        versionCode = Android.versionCode
        versionName = Android.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
        )
    }
}

dependencies {
    // --- Google libraries (prepare for pain when using them!) ---
    // AndroidX libraries
    implementation(Library.App.AndroidX.core)
    implementation(Library.App.AndroidX.splashScreen)
    implementation(Library.App.AndroidX.appCompat)
    implementation(Library.App.AndroidX.constraintLayout)
    implementation(Library.App.AndroidX.preference)
    implementation(Library.App.AndroidX.liveData)
    implementation(Library.App.AndroidX.navigationFragment)
    implementation(Library.App.AndroidX.navigationUI)
    implementation(Library.App.AndroidX.camera)
    implementation(Library.App.AndroidX.cameraLifecycle)
    implementation(Library.App.AndroidX.cameraView)

    // com.google.* libraries
    implementation(Library.App.material)
    implementation(Library.App.hilt)
    kapt(Library.App.hiltCompiler)

    // --- third parties ---
    implementation(Library.App.viewBindingQOL)
    implementation(Library.App.glide)
    kapt(Library.App.glideCompiler)
    implementation(Library.App.flowPreferences)

    // --- Testing libraries ---
    testImplementation(Library.App.Testing.junit)
    androidTestImplementation(Library.App.Testing.junitAndroid)
    androidTestImplementation(Library.App.Testing.espresso)

    // --- desugaring ---
    coreLibraryDesugaring(Library.App.desugar)
}
