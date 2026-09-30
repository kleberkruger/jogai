pluginManagement {
    includeBuild("build-logic")

    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "jogai"

include("domain", "application", "data", "presentation")
