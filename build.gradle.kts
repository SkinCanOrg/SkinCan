buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Library.Root.androidGradlePlugin)
        classpath(Library.Root.kotlinGradlePlugin)
        classpath(Library.Root.hiltGradlePlugin)
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}