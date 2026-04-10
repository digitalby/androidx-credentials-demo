import java.util.Base64

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

// Signing: read from environment variables populated by 1Password in CI,
// or by the developer running `op run -- ./gradlew assembleRelease` locally.
// When variables are absent the release build remains unsigned (debug-signing
// is used automatically by AGP for local runs).
val signingKeystoreB64: String? = System.getenv("SIGNING_KEYSTORE_B64")
val signingStorePassword: String? = System.getenv("SIGNING_STORE_PASSWORD")
val signingKeyAlias: String? = System.getenv("SIGNING_KEY_ALIAS")
val signingKeyPassword: String? = System.getenv("SIGNING_KEY_PASSWORD")

android {
    namespace = "info.yuryv.androidx_credentials_demo"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "info.yuryv.androidx_credentials_demo"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    if (signingKeystoreB64 != null &&
        signingStorePassword != null &&
        signingKeyAlias != null &&
        signingKeyPassword != null
    ) {
        val keystoreFile =
            layout.buildDirectory
                .file("signing/release.jks")
                .get()
                .asFile
        keystoreFile.parentFile.mkdirs()
        // Decode from the base64 field stored in 1Password.
        keystoreFile.writeBytes(Base64.getDecoder().decode(signingKeystoreB64))

        signingConfigs {
            create("release") {
                storeFile = keystoreFile
                storePassword = signingStorePassword
                keyAlias = signingKeyAlias
                keyPassword = signingKeyPassword
            }
        }
    }

    buildTypes {
        debug {
            buildConfigField(
                "String",
                "GOOGLE_WEB_CLIENT_ID",
                "\"YOUR_WEB_CLIENT_ID.apps.googleusercontent.com\"",
            )
        }
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField(
                "String",
                "GOOGLE_WEB_CLIENT_ID",
                "\"YOUR_WEB_CLIENT_ID.apps.googleusercontent.com\"",
            )
            val releaseSigningConfig = signingConfigs.findByName("release")
            if (releaseSigningConfig != null) {
                signingConfig = releaseSigningConfig
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    testOptions {
        unitTests {
            // Required for Robolectric to access Android resources and assets in unit tests.
            isIncludeAndroidResources = true
            all { test ->
                // Forward -Proborazzi.test.record=true (Gradle property) to the test JVM
                // so that captureRoboImage actually writes files.
                val recordMode = project.findProperty("roborazzi.test.record") as? String ?: "false"
                test.systemProperty("roborazzi.test.record", recordMode)
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.google.identity.googleid)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.json)
    testImplementation(libs.junit)
    testImplementation(libs.roborazzi)
    testImplementation(libs.roborazzi.compose)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation(libs.androidx.compose.ui.test.manifest)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
