@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  alias(libs.plugins.android.app)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.jetbrains.compose)
  alias(libs.plugins.kotlin.compose)
}

android {
  namespace = "com.hoc081098.channeleventbus.sample.kmp.compose.android"
  compileSdk = libs.versions.sample.android.compile.get().toInt()
  defaultConfig {
    applicationId = "com.hoc081098.channeleventbus.sample.kmp.compose.android"
    minSdk = libs.versions.android.min.get().toInt()
    targetSdk = libs.versions.sample.android.target.get().toInt()
    versionCode = 1
    versionName = "1.0"
  }
  buildFeatures {
    buildConfig = true
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
    sourceCompatibility = JavaVersion.toVersion(libs.versions.java.target.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.java.target.get())
  }
  kotlinOptions {
    jvmTarget = JavaVersion.toVersion(libs.versions.java.target.get()).toString()
  }
}

kotlin {
  jvmToolchain {
    languageVersion = JavaLanguageVersion.of(libs.versions.java.toolchain.get())
    vendor = JvmVendorSpec.AZUL
  }
}

dependencies {
  // Compose app
  implementation(projects.sample.standaloneComposeMultiplatform.composeApp)

  implementation(libs.androidx.activity.compose)
  implementation(libs.koin.androidx.compose)

  implementation(libs.flowExt)

  implementation(libs.timber)
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
