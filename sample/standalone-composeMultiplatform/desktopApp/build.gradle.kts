import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.jetbrains.compose)
  alias(libs.plugins.kotlin.compose)
}

kotlin {
  jvmToolchain {
    languageVersion = JavaLanguageVersion.of(17)
    vendor = JvmVendorSpec.AZUL
  }

  jvm("desktop") {
    compilations.all {
      kotlinOptions {
        jvmTarget = "11"
      }
    }
  }

  sourceSets {
    commonMain.dependencies {
      // Channel event bus
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
  enableStrongSkippingMode = true
}
