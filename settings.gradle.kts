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
  }
}

rootProject.name = "kotlin-channel-event-bus"
include(":channel-event-bus")
include(":sample:app")

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version("0.7.0")
}
