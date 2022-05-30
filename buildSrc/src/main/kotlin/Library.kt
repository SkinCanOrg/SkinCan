object Library {
    object Version {
        const val hilt = "2.42"
        const val navigation = "2.4.2"
        const val glide = "4.13.2"
        const val cameraX = "1.1.0-rc01"
        const val retrofit = "2.9.0"
        const val okhttp = "4.9.3"
    }

    object Root {
        // Gradle Plugin 7.1+ doesn't work on IDEA yet.
        // This will probably fixed on IDEA 2022.2.1 EAP 3.
        const val androidGradlePlugin = "com.android.tools.build:gradle:7.0.4"
        const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21"
        const val hiltGradlePlugin= "com.google.dagger:hilt-android-gradle-plugin:${Version.hilt}"
    }

    object App {
        object AndroidX {
            const val core = "androidx.core:core-ktx:1.7.0"
            const val splashScreen = "androidx.core:core-splashscreen:1.0.0-rc01"
            const val appCompat = "androidx.appcompat:appcompat:1.4.1"
            const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.4"
            const val preference = "androidx.preference:preference-ktx:1.2.0"
            const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:2.4.1"
            const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:${Version.navigation}"
            const val navigationUI = "androidx.navigation:navigation-ui-ktx:${Version.navigation}"
            const val camera = "androidx.camera:camera-camera2:${Version.cameraX}"
            const val cameraLifecycle = "androidx.camera:camera-lifecycle:${Version.cameraX}"
            const val cameraView = "androidx.camera:camera-view:${Version.cameraX}"
        }

        const val material = "com.google.android.material:material:1.6.0"
        const val hilt = "com.google.dagger:hilt-android:${Version.hilt}"
        const val hiltCompiler = "com.google.dagger:hilt-compiler:${Version.hilt}"
        const val viewBindingQOL = "com.github.kirich1409:viewbindingpropertydelegate:1.5.6"
        const val glide = "com.github.bumptech.glide:glide:${Version.glide}"
        const val glideCompiler = "com.github.bumptech.glide:compiler:${Version.glide}"
        const val flowPreferences = "com.fredporciuncula:flow-preferences:1.7.0"
        const val retrofit = "com.squareup.retrofit2:retrofit:${Version.retrofit}"
        const val retrofitScalars = "com.squareup.retrofit2:converter-scalars:${Version.retrofit}"
        const val retrofitGson = "com.squareup.retrofit2:converter-gson:${Version.retrofit}"
        const val okhttp = "com.squareup.okhttp3:okhttp:${Version.okhttp}"
        const val okhttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Version.okhttp}"

        object Testing {
            const val junit = "junit:junit:4.13.2"
            const val junitAndroid = "androidx.test.ext:junit:1.1.3"
            const val espresso = "androidx.test.espresso:espresso-core:3.4.0"
        }

        const val desugar = "com.android.tools:desugar_jdk_libs:1.1.5"
    }
}
