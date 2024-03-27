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
      implementation(libs.androidx.compose.ui.tooling.preview)
      implementation(libs.androidx.activity.compose)

      // Koin
      implementation(libs.koin.android)
      implementation(libs.koin.androidx.compose)

      // Coroutines
      implementation(libs.coroutines.android)
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
      implementation(libs.kmp.viewmodel)
      implementation(libs.kmp.viewmodel.savedstate)
      implementation(libs.kmp.viewmodel.compose)
      implementation(libs.kmp.viewmodel.koin.compose)
      implementation(libs.solivagant.navigation)

      // Koin
      implementation(libs.koin.core)
      implementation(libs.koin.compose)

      // Coroutines & FlowExt
      implementation(libs.coroutines.core)
      implementation(libs.flowExt)

      // Immutable collections
      implementation(libs.kotlinx.collections.immutable)

      // Napier logger
      implementation(libs.napier)
    }
    desktopMain.dependencies {
      implementation(compose.desktop.currentOs)

      // Coroutines
      implementation(libs.coroutines.swing)
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
dependencies {
  implementation(project(":channel-event-bus"))
  implementation(project(":sample:standalone-androidApp"))
}
