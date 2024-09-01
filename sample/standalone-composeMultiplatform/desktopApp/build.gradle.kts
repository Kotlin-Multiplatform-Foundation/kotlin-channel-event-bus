import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.jetbrains.compose)
  alias(libs.plugins.kotlin.compose)
}

kotlin {
  jvmToolchain {
    languageVersion = JavaLanguageVersion.of(libs.versions.java.toolchain.get())
    vendor = JvmVendorSpec.AZUL
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
    commonMain.dependencies {
      // Compose app
      implementation(projects.sample.standaloneComposeMultiplatform.composeApp)

      implementation(compose.runtime)
      implementation(compose.foundation)
      implementation(compose.material3)
      implementation(compose.ui)
      implementation(compose.components.resources)
    }

    val desktopMain by getting
    desktopMain.dependencies {
      implementation(compose.desktop.currentOs)

      // Coroutines
      implementation(libs.coroutines.swing)
    }
  }
}

compose.desktop {
  application {
    mainClass = "MainKt"

    nativeDistributions {
      targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
      packageName = "com.hoc081098.channeleventbus.sample.kmp.compose"
      packageVersion = "1.0.0"
    }
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
