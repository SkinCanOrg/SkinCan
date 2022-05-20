plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

@Suppress("UnstableApiUsage")
android {
    compileSdk = 31

    defaultConfig {
        applicationId = "io.github.skincanorg.skincan"
        minSdk = 23
        targetSdk = 31
        versionCode = 1
        versionName = "0.1"

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
    }
}

dependencies {
    // --- Google libraries (prepare for pain when using them!) ---
    // AndroidX libraries
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.core:core-splashscreen:1.0.0-rc01")
    implementation("androidx.appcompat:appcompat:1.4.1")

    implementation("androidx.constraintlayout:constraintlayout:2.1.3")

    implementation("androidx.preference:preference-ktx:1.2.0")

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.1")

    val navigationVersion = "2.4.2"
    implementation("androidx.navigation:navigation-fragment-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navigationVersion")

    // com.google.* libraries
    implementation("com.google.android.material:material:1.6.0")
    val hiltVersion = "2.42"
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-compiler:$hiltVersion")

    // --- third parties ---
    // view binding QOL
    implementation("com.github.kirich1409:viewbindingpropertydelegate:1.5.6")

    // --- Testing libraries ---
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}