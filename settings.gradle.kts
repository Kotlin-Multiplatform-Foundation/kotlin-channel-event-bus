enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
  }
}

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
    maven(url = "https://androidx.dev/storage/compose-compiler/repository/")
  }
}

rootProject.name = "kotlin-channel-event-bus"
include(":channel-event-bus")
include(":sample:standalone-androidApp")
include(":sample:standalone-composeMultiplatform:composeApp")

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version ("0.8.0")
}
