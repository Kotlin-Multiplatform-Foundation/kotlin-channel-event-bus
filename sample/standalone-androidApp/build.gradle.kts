@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  alias(libs.plugins.android.app)
  alias(libs.plugins.kotlin.android)
}

android {
  namespace = "com.hoc081098.channeleventbus.sample.android"
  compileSdk = libs.versions.sample.android.compile.get().toInt()
  defaultConfig {
    applicationId = "com.hoc081098.channeleventbus.sample.android"
    minSdk = libs.versions.android.min.get().toInt()
    targetSdk = libs.versions.sample.android.target.get().toInt()
    versionCode = 1
    versionName = "1.0"
  }
  buildFeatures {
    compose = true
    buildConfig = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.androidx.compose.compiler.get()
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

dependencies {
  implementation(project(":channel-event-bus"))

  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.lifecycle.runtime.compose)

  implementation(libs.androidx.compose.ui.ui)
  debugImplementation(libs.androidx.compose.ui.tooling)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.compose.foundation)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.material)
  implementation(libs.androidx.compose.runtime)
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.navigation.compose)

  implementation(libs.koin.androidx.compose)
  implementation(libs.coil.compose)
  implementation(libs.flowExt)

  implementation(libs.kotlinx.collections.immutable)
  implementation(libs.kmp.viewmodel.savedstate)
  implementation(libs.timber)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
  kotlinOptions {
    if (project.findProperty("composeCompilerReports") == "true") {
      freeCompilerArgs = freeCompilerArgs + listOf(
        "-P",
        "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${project.buildDir.absolutePath}/compose_compiler",
      )
    }
    if (project.findProperty("composeCompilerMetrics") == "true") {
      freeCompilerArgs = freeCompilerArgs + listOf(
        "-P",
        "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${project.buildDir.absolutePath}/compose_compiler",
      )
    }
  }
}
