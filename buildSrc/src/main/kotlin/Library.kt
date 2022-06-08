object Library {
    object Version {
        const val hilt = "2.42"
        const val navigation = "2.4.2"
        const val glide = "4.13.2"
        const val cameraX = "1.1.0-rc01"
        const val retrofit = "2.9.0"
        const val okhttp = "4.9.3"
        const val aboutLibraries = "10.3.0"
        const val sqlDelight = "1.5.3"
        const val adt = "7.2.0"
        const val kotlinAndroid = "1.6.21"
        const val googleServices = "4.3.10"
    }

    object Root {
        // Gradle Plugin 7.1+ doesn't work on IDEA 2022.1.x.
        // This issue has been fixed on IDEA 2022.2.1 EAP 3 (build 222.2889).
        const val androidAppGradlePlugin = "com.android.application"
        const val androidLibGradlePlugin = "com.android.library"
        const val kotlinGradlePlugin = "org.jetbrains.kotlin.android"
        const val hiltGradlePlugin = "com.google.dagger.hilt.android"
        const val googleServices = "com.google.gms.google-services"
    }

    object App {
        object AndroidX {
            const val core = "androidx.core:core-ktx:1.8.0"
            const val splashScreen = "androidx.core:core-splashscreen:1.0.0-rc01"
            const val appCompat = "androidx.appcompat:appcompat:1.6.0-alpha04"
            const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.4"
            const val preference = "androidx.preference:preference-ktx:1.2.0"
            const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:2.4.1"
            const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:${Version.navigation}"
            const val navigationUI = "androidx.navigation:navigation-ui-ktx:${Version.navigation}"
            const val camera = "androidx.camera:camera-camera2:${Version.cameraX}"
            const val cameraLifecycle = "androidx.camera:camera-lifecycle:${Version.cameraX}"
            const val cameraView = "androidx.camera:camera-view:${Version.cameraX}"
            const val paging = "androidx.paging:paging-runtime-ktx:3.1.1"
        }

        const val material = "com.google.android.material:material:1.7.0-alpha02"
        const val hilt = "com.google.dagger:hilt-android:${Version.hilt}"
        const val hiltCompiler = "com.google.dagger:hilt-compiler:${Version.hilt}"
        const val googleAuth = "com.google.android.gms:play-services-auth:20.2.0"

        object Firebase {
            const val bom = "com.google.firebase:firebase-bom:30.1.0"
            const val analytics = "com.google.firebase:firebase-analytics-ktx:21.0.0"
            const val auth = "com.google.firebase:firebase-auth-ktx:21.0.5"
            const val firestore = "com.google.firebase:firebase-firestore-ktx:24.1.2"
            const val storage = "com.google.firebase:firebase-storage-kts:20.0.1"
            const val machineLearning = "com.google.firebase:firebase-ml-modeldownloader-ktx:24.0.3"
        }

        const val tensorflowLite = "org.tensorflow:tensorflow-lite:2.8.0"
        const val tensorflowLiteSupport = "org.tensorflow:tensorflow-lite-support:0.4.0"
        const val viewBindingQOL = "com.github.kirich1409:viewbindingpropertydelegate:1.5.6"
        const val glide = "com.github.bumptech.glide:glide:${Version.glide}"
        const val glideCompiler = "com.github.bumptech.glide:compiler:${Version.glide}"
        const val flowPreferences = "com.fredporciuncula:flow-preferences:1.7.0"
        const val aboutLibrariesCore = "com.mikepenz:aboutlibraries-core:${Version.aboutLibraries}"
        const val aboutLibrariesUI = "com.mikepenz:aboutlibraries:${Version.aboutLibraries}"
        const val sqlDelightAndroid = "com.squareup.sqldelight:android-driver:${Version.sqlDelight}"
        const val sqlDelightPaging = "com.squareup.sqldelight:android-paging3-extensions:${Version.sqlDelight}"

        object Testing {
            const val junit = "junit:junit:4.13.2"
            const val junitAndroid = "androidx.test.ext:junit:1.1.3"
            const val espresso = "androidx.test.espresso:espresso-core:3.4.0"
        }

        const val desugar = "com.android.tools:desugar_jdk_libs:1.1.5"
    }
}
