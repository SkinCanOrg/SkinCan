// TODO: Check out gradle "version catalog"
object Library {
    // common versions
    const val hiltVersion = "2.42"

    object Root {
        // Gradle Plugin 7.1+ doesn't work on IDEA 2022.1.1 for some reason
        const val androidGradlePlugin = "com.android.tools.build:gradle:7.0.4"
        const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21"
        const val hiltGradlePlugin= "com.google.dagger:hilt-android-gradle-plugin:$hiltVersion"
    }

    object App {
        // versions
        const val navigationVersion = "2.4.2"
        const val glideVersion = "4.13.2"
        const val cameraxVersion = "1.1.0-rc01"

        object AndroidX {
            // library locations
            const val core = "androidx.core:core-ktx:1.7.0"
            const val splashScreen = "androidx.core:core-splashscreen:1.0.0-rc01"
            const val appCompat = "androidx.appcompat:appcompat:1.4.1"
            const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.4"
            const val preference = "androidx.preference:preference-ktx:1.2.0"
            const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:2.4.1"
            const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:$navigationVersion"
            const val navigationUI = "androidx.navigation:navigation-ui-ktx:$navigationVersion"
            const val camera = "androidx.camera:camera-camera2:$cameraxVersion"
            const val cameraLifecycle = "androidx.camera:camera-lifecycle:$cameraxVersion"
            const val cameraView = "androidx.camera:camera-view:$cameraxVersion"
        }

        const val material = "com.google.android.material:material:1.6.0"
        const val hilt = "com.google.dagger:hilt-android:$hiltVersion"
        const val hiltCompiler = "com.google.dagger:hilt-compiler:$hiltVersion"
        const val viewBindingQOL = "com.github.kirich1409:viewbindingpropertydelegate:1.5.6"
        const val glide = "com.github.bumptech.glide:glide:$glideVersion"
        const val glideCompiler = "com.github.bumptech.glide:compiler:$glideVersion"

        object Testing {
            const val junit = "junit:junit:4.13.2"
            const val junitAndroid = "androidx.test.ext:junit:1.1.3"
            const val espresso = "androidx.test.espresso:espresso-core:3.4.0"
        }

        const val desugar = "com.android.tools:desugar_jdk_libs:1.1.5"
    }
}