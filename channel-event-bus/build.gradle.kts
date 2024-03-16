@file:Suppress("ClassName")

import java.net.URL
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.vanniktech.maven.publish)
  alias(libs.plugins.dokka)
  alias(libs.plugins.kotlinx.binary.compatibility.validator)
  alias(libs.plugins.kotlinx.kover)
}

kotlin {
  explicitApi()

  jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(libs.versions.java.toolchain.get()))
    vendor.set(JvmVendorSpec.AZUL)
  }

  jvm {
    compilations.configureEach {
      compilerOptions.configure {
        jvmTarget.set(JvmTarget.fromTarget(libs.versions.java.target.get()))
      }
    }
  }

  js(IR) {
    moduleName = property("POM_ARTIFACT_ID")!!.toString()
    compilations.configureEach {
      compilerOptions.configure {
        sourceMap.set(true)
        moduleKind.set(org.jetbrains.kotlin.gradle.dsl.JsModuleKind.MODULE_COMMONJS)
      }
    }
    browser()
    nodejs()
  }
  @OptIn(org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl::class)
  wasmJs {
    // Module name should be different from the one from JS
    // otherwise IC tasks that start clashing different modules with the same module name
    moduleName = property("POM_ARTIFACT_ID")!!.toString() + "Wasm"
    browser()
    nodejs()
  }

  // According to https://kotlinlang.org/docs/native-target-support.html
  // Tier 1: macosX64, macosArm64, iosSimulatorArm64, iosX64
  // Tier 2: linuxX64, linuxArm64,
  //         watchosSimulatorArm64, watchosX64, watchosArm32, watchosArm64,
  //         tvosSimulatorArm64, tvosX64, tvosArm64,
  //         iosArm64
  // Tier 3: androidNativeArm32, androidNativeArm64, androidNativeX86, androidNativeX64,
  //         mingwX64,
  //         watchosDeviceArm64

  iosArm64()
  iosX64()
  iosSimulatorArm64()

  macosX64()
  macosArm64()
  mingwX64()
  linuxX64()
  linuxArm64()

  tvosX64()
  tvosSimulatorArm64()
  tvosArm64()

  watchosArm32()
  watchosArm64()
  watchosX64()
  watchosSimulatorArm64()
  watchosDeviceArm64()

  androidNativeArm32()
  androidNativeArm64()
  androidNativeX86()
  androidNativeX64()

  sourceSets {
    commonMain {
      dependencies {
        api(libs.coroutines.core)
      }
    }
    commonTest {
      dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))

        implementation(libs.coroutines.test)
        implementation(libs.flowExt)
      }
    }

    jsTest {
      dependencies {
        implementation(kotlin("test-js"))
      }
    }

    val wasmJsTest by getting {
      dependencies {
        implementation(kotlin("test-wasm-js"))
      }
    }

    jvmTest {
      dependencies {
        implementation(kotlin("test-junit"))
      }
    }
  }

  sourceSets.matching { it.name.contains("Test") }.all {
    languageSettings {
      optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
    }
  }
}

mavenPublishing {
  publishToMavenCentral(com.vanniktech.maven.publish.SonatypeHost.S01, automaticRelease = true)
  signAllPublications()
}

tasks.withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
  dokkaSourceSets {
    configureEach {
      externalDocumentationLink("https://kotlinlang.org/api/kotlinx.coroutines/")

      sourceLink {
        localDirectory.set(projectDir.resolve("src"))
        remoteUrl.set(URL("https://github.com/hoc081098/kotlin-channel-event-bus/tree/master/channel-event-bus/src"))
        remoteLineSuffix.set("#L")
      }
    }
  }
}
