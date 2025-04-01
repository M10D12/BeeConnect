plugins {
    // Add Kotlin plugin if not already present
    kotlin("android") version "1.9.0" apply false
    id("com.android.application") version "8.3.0"
}

buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:7.1.3")
        classpath("com.google.gms:google-services:4.3.2")
    }
}

android {
    namespace = "com.example.beeconnect"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.teuprojeto.beeconnect"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    // resto da config...
}
