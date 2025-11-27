plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

// Usa un directorio de build fuera de OneDrive para evitar bloqueos de archivos
buildDir = file("${rootProject.projectDir}/.local-build/app")

android {
    namespace = "com.example.usagi_tienda_app"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.usagi_tienda_app"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // Propiedades y validación de firma de release
    val releaseStoreFileProp = (project.findProperty("RELEASE_STORE_FILE") as String?) ?: "keystore/release.jks"
    val releaseStoreFile = file(releaseStoreFileProp)
    val releaseStorePassword = (project.findProperty("RELEASE_STORE_PASSWORD") ?: "UNSET").toString()
    val releaseKeyAlias = (project.findProperty("RELEASE_KEY_ALIAS") ?: "key0").toString()
    val releaseKeyPassword = (project.findProperty("RELEASE_KEY_PASSWORD") ?: "UNSET").toString()
    val hasReleaseSigningCreds = releaseStoreFile.exists() &&
            releaseStorePassword != "UNSET" &&
            releaseKeyAlias.isNotBlank() &&
            releaseKeyPassword != "UNSET"

    // Firma de APK (release) - definir antes de buildTypes
    signingConfigs {
        create("release") {
            storeFile = releaseStoreFile
            storePassword = releaseStorePassword
            keyAlias = releaseKeyAlias
            keyPassword = releaseKeyPassword
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            if (hasReleaseSigningCreds) {
                signingConfig = signingConfigs.getByName("release")
            } else {
                println("Release sin firma: keystore/credenciales no configuradas.")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    // Configuración de pruebas unitarias con JUnit 5
    testOptions {
        unitTests.isReturnDefaultValues = true
        unitTests.all {
            it.useJUnitPlatform()
            it.testLogging {
                events("passed", "skipped", "failed")
            }
        }
    }

}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.text)

    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.windowsize)
    implementation(libs.androidx.navigation.compose)

    // CameraX
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)

    // Room + KSP
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // ML Kit - Barcode Scanning
    implementation(libs.google.mlkit.barcode.scanning)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Retrofit 
    implementation(libs.squareup.retrofit2)
    implementation(libs.squareup.retrofit2.converter.moshi)

    // OkHttp logging
    implementation(libs.squareup.okhttp3.logging.interceptor)

    // Coil for image loading in Compose
    implementation(libs.coil.compose)

    // JUnit 4 (existente)
    testImplementation(libs.junit)

    // JUnit 5
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)

    // Kotest (runner JUnit5 y assertions)
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.assertions.core)

    // MockK para mocks
    testImplementation(libs.mockk)

    // Coroutines test (alineado con 1.7.1 del proyecto)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

// Configuración de Room para exportar esquemas y evitar warnings (KSP)
ksp {
    arg("room.schemaLocation", "${project.projectDir}/schemas")
    arg("room.incremental", "true")
    arg("room.expandProjection", "true")
}
