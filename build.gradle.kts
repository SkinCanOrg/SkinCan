buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Library.Root.androidGradlePlugin)
        classpath(Library.Root.kotlinGradlePlugin)
        classpath(Library.Root.hiltGradlePlugin)
        classpath(Library.Root.googleServices)
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
