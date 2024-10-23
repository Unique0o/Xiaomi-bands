import com.google.protobuf.gradle.*

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.serialization.kotlin)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.protobuf)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.logifitappp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.info.logifit.pe"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.7"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/INDEX.LIST"
        }
    }
    sourceSets {
        getByName("main") {
            java {
                java.srcDirs("build/generated/source/proto/main/java")
            }

            kotlin {
                java.srcDirs("build/generated/source/proto/main/kotlin")
            }

            proto {
                java.srcDir("src/main/proto")
            }
        }
    }
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.navigation.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.material.icons)
    implementation(libs.coil.compose)
    implementation(libs.play.services.wearable)
    implementation(libs.permissions)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.core.i18n)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.logback.classic)
    implementation(libs.retrofit)
    implementation(libs.convert.gson)
    implementation(libs.androidx.preference)
    implementation(libs.protobuf.java)
    implementation(libs.protobuf.kotlin)
    implementation(libs.bcpkix.jdk15to18)
    implementation(libs.commons.lang3)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.localbroadcastmanager)
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.lottie.compose)
    implementation(libs.firebase.messaging.ktx)
    implementation(libs.androidx.material)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.20.1"
    }

    plugins {
        create("javalite") {
            artifact = "com.google.protobuf:protoc-gen-javalite:3.0.0"
        }
    }

    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                id("java") {
                    option("lite")
                }
                id("kotlin") {
                    option("lite")
                }
            }
        }
    }
}