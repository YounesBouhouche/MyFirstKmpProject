import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinx.serialization)
}

val generateApiKey by tasks.registering {
    val outDir = layout.buildDirectory.dir("generated/kotlin/org/example/project")
    outputs.dir(outDir)
    doLast {
        val localFile = rootProject.file("local.properties")
        val fromLocal = if (localFile.exists()) {
            Properties().also { props -> localFile.inputStream().use { props.load(it) } }
                .getProperty("API_KEY")
        } else null

        val apiKey = fromLocal
            ?: System.getenv("API_KEY")
            ?: (project.findProperty("apiKey") as? String)
            ?: throw GradleException("API key not found. Set `API_KEY` in local.properties, env `API_KEY`, or pass -PapiKey=...")

        val file = outDir.get().file("ApiKeys.kt").asFile
        file.parentFile.mkdirs()
        file.writeText(
            """
            package org.example.project

            object ApiKeys {
                const val API_KEY: String = "$apiKey"
            }
            """.trimIndent()
        )
    }
}

// Ensure Kotlin compile tasks run after generation
tasks.matching { it.name.startsWith("compileKotlin") }.configureEach {
    dependsOn(generateApiKey)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    jvm()
    
    sourceSets {
        val commonMain by getting {
            kotlin.srcDir(layout.buildDirectory.dir("generated/kotlin"))
        }
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.androidx.compose)
            implementation(libs.koin.androidx.compose.navigation)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.ktor.client.android)
            implementation(libs.coil.network.okhttp)
            implementation("com.github.khushpanchal:Ketch:2.0.5")
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(libs.material.icons.extended)
            implementation(libs.material3)
            implementation(libs.adaptive)
            implementation(libs.adaptive.layout)
            implementation(libs.adaptive.navigation)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            api(libs.koin.core)
            api(libs.koin.compose)
            api(libs.koin.compose.viewmodel)
            api(libs.koin.compose.viewmodel.navigation)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
            implementation(libs.material.kolor)
            implementation(libs.androidx.datastore)
            implementation(libs.androidx.datastore.datastore.preferences)
        }
        nativeMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.oshi.core)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.ktor.client.java)
            implementation("org.asynchttpclient:async-http-client:3.0.3")
        }
    }
}

android {
    namespace = "org.example.project"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.example.project"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
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
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "org.example.project.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.example.project"
            packageVersion = "1.0.0"
        }
    }
}

android {
    namespace = "org.example.project"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    android.buildFeatures.buildConfig = true

    defaultConfig {
        val apiKey = project.loadLocalProperty(
            path = "local.properties",
            propertyName = "API_KEY",
        )
        buildConfigField("String", "apiKey", "\"$apiKey\"")

        applicationId = "org.example.project"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}


fun Project.loadLocalProperty(
    path: String,
    propertyName: String,
): String {
    val localProperties = Properties()
    val localPropertiesFile = project.rootProject.file(path)
    if (!localPropertiesFile.exists())
        throw GradleException("can not find property : $propertyName")
    localProperties.load(localPropertiesFile.inputStream())
    return localProperties.getProperty(propertyName)
        ?: throw GradleException("can not find property : $propertyName")
}