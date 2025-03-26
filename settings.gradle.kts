pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            // Ensure 'from' is only called once
            from(files("gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "BeeConnect"
include(":app")
