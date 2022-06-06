plugins {
    id(Library.Root.androidAppGradlePlugin) version Library.Version.adt apply false
    id(Library.Root.androidLibGradlePlugin) version Library.Version.adt apply false
    id(Library.Root.kotlinGradlePlugin) version Library.Version.kotlinAndroid apply false
    id(Library.Root.hiltGradlePlugin) version Library.Version.hilt apply false
    id(Library.Root.googleServices) version Library.Version.googleServices apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
