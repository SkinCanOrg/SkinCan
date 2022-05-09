buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.4")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.20")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.41")
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}