// Top-level build file where you can add configuration options common to all subprojects/modules.
buildscript {
    repositories {
        google()
    }
    dependencies {
        val nav_version = "2.9.8"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
    }
}

plugins {
    id("com.android.application") version "9.4.1" apply false
    id("org.jetbrains.kotlin.android") version "2.4.10" apply false
    id("com.google.devtools.ksp") version "2.3.6"
}