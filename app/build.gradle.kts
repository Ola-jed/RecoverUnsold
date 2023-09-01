val compose_version = "1.4.3"
val moshi_version = "1.14.0"
val material3_version = "1.1.1"

plugins {
    id("com.android.application")
    id("com.google.protobuf") version "0.9.0"
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.gms.google-services")
    id("com.google.dagger.hilt.android")
    id("com.google.android.gms.oss-licenses-plugin")
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace = "com.ola.recoverunsold"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.ola.recoverunsold"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = compose_version
    }
    packagingOptions {
        resources {
            exclude("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.compose.ui:ui:$compose_version")
    implementation("androidx.compose.material3:material3:$material3_version")
    implementation("androidx.compose.ui:ui-tooling-preview:$compose_version")
    implementation("androidx.compose.material:material-icons-extended:$compose_version")
    implementation("androidx.compose.ui:ui-text-google-fonts:$compose_version")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.navigation:navigation-compose:2.6.0")
    implementation("androidx.datastore:datastore:1.0.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation("com.squareup.moshi:moshi:$moshi_version")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("com.google.protobuf:protobuf-javalite:3.18.0")
    implementation("com.google.maps.android:maps-compose:2.4.0")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("io.coil-kt:coil-compose:2.1.0")
    implementation("com.google.android.libraries.maps:maps:3.1.0-beta")
    implementation("com.google.maps.android:maps-v3-ktx:3.4.0")
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.google.accompanist:accompanist-permissions:0.21.1-beta")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.21.1-beta")
    implementation("com.google.accompanist:accompanist-swiperefresh:0.21.1-beta")
    implementation("com.google.firebase:firebase-messaging:23.1.2")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("com.google.dagger:hilt-android:2.43.2")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation("com.airbnb.android:lottie-compose:5.2.0")
    implementation("io.sentry:sentry-android:6.4.1")
    implementation("com.google.android.gms:play-services-oss-licenses:17.0.1")
    implementation("io.github.bytebeats:compose-charts:0.1.2")
    implementation("com.github.kkiapay:android-sdk:1.3.0")

    kapt("com.google.dagger:hilt-compiler:2.43.2")
    kapt("androidx.hilt:hilt-compiler:1.0.0")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:$moshi_version")

    annotationProcessor("com.google.dagger:hilt-compiler:2.43.2")

    implementation(platform("com.google.firebase:firebase-bom:30.2.0"))

    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$compose_version")

    debugImplementation("androidx.compose.ui:ui-tooling:$compose_version")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$compose_version")

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")
}

kapt {
    correctErrorTypes = true
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.14.0"
    }

    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
            }
        }
    }
}