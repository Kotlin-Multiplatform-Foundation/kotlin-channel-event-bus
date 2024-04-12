plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.jetbrains.compose)
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.parcelize)
}

kotlin {
  jvmToolchain {
    languageVersion = JavaLanguageVersion.of(17)
    vendor = JvmVendorSpec.AZUL
  }

  androidTarget {
    compilations.all {
      kotlinOptions {
        jvmTarget = "11"
      }
    }
  }

  jvm("desktop") {
    compilations.all {
      kotlinOptions {
        jvmTarget = "11"
      }
    }
  }

  sourceSets {
    val desktopMain by getting

    androidMain.dependencies {
      api(libs.androidx.compose.ui.tooling.preview)
      api(libs.androidx.activity.compose)

      // Koin
      api(libs.koin.android)
      api(libs.koin.androidx.compose)
    }
    commonMain.dependencies {
      // Channel event bus
      implementation(project(":channel-event-bus"))

      implementation(compose.runtime)
      implementation(compose.foundation)
      implementation(compose.material3)
      implementation(compose.ui)
      implementation(compose.components.resources)

      // KMP View Model & Solivagant navigation
      api(libs.kmp.viewmodel)
      api(libs.kmp.viewmodel.savedstate)
      api(libs.kmp.viewmodel.compose)
      api(libs.kmp.viewmodel.koin.compose)
      api(libs.solivagant.navigation)

      // Koin
      api(libs.koin.core)
      api(libs.koin.compose)

      // Coroutines & FlowExt
      api(libs.coroutines.core)
      api(libs.flowExt)

      // Immutable collections
      api(libs.kotlinx.collections.immutable)

      // Napier logger
      api(libs.napier)
    }
    desktopMain.dependencies {
      implementation(compose.desktop.currentOs)
    }
  }
}

android {
  namespace = "com.hoc081098.channeleventbus.sample.kmp.compose"
  compileSdk = libs.versions.sample.android.compile.get().toInt()

  sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
  sourceSets["main"].res.srcDirs("src/androidMain/res")
  sourceSets["main"].resources.srcDirs("src/commonMain/resources")

  defaultConfig {
    minSdk = libs.versions.android.min.get().toInt()
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  buildFeatures {
    buildConfig = true
  }
  dependencies {
    debugImplementation(libs.androidx.compose.ui.tooling)
  }
}
