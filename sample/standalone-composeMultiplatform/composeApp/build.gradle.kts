import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.jetbrains.compose)
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.parcelize)
  alias(libs.plugins.kotlin.compose)
}

kotlin {
  jvmToolchain {
    languageVersion = JavaLanguageVersion.of(libs.versions.java.toolchain.get())
    vendor = JvmVendorSpec.AZUL
  }

  androidTarget {
    compilations.configureEach {
      compileTaskProvider.configure {
        compilerOptions {
          jvmTarget = JvmTarget.fromTarget(libs.versions.java.target.get())
        }
      }
    }
  }

  jvm("desktop") {
    compilations.configureEach {
      compileTaskProvider.configure {
        compilerOptions {
          jvmTarget = JvmTarget.fromTarget(libs.versions.java.target.get())
        }
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
      implementation(projects.channelEventBus)

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

composeCompiler {
  featureFlags.addAll(
    org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag.OptimizeNonSkippingGroups,
  )

  val composeCompilerDir = layout.buildDirectory.dir("compose_compiler")
  if (project.findProperty("composeCompilerReports") == "true") {
    reportsDestination = composeCompilerDir
  }
  if (project.findProperty("composeCompilerMetrics") == "true") {
    metricsDestination = composeCompilerDir
  }
}
