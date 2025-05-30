plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.androidproject"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.mycompany.daguconnect"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.foundation.android)
    implementation(libs.androidx.storage)
    implementation(libs.androidx.paging.compose.android)
    //navigation
    val nav_version = "2.8.4"
    implementation("androidx.navigation:navigation-compose:$nav_version")

    implementation("androidx.compose.material:material-icons-extended")
    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation ("com.squareup.retrofit2:adapter-rxjava2:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation ("org.json:json:20231013")
    implementation ("androidx.paging:paging-runtime-ktx:3.2.1")
    implementation ("androidx.paging:paging-compose:3.3.0")
    implementation ("androidx.core:core-ktx:1.12.0")
    implementation("io.coil-kt:coil-compose:2.6.0")
    //livedata
    implementation("androidx.compose.runtime:runtime-livedata:1.5.3")
    implementation("org.osmdroid:osmdroid-android:6.1.20")

    implementation ("androidx.constraintlayout:constraintlayout-compose:1.0.1")

    implementation ("com.google.accompanist:accompanist-flowlayout:0.31.2-alpha")
    //Firebase Cloud Messaging
    implementation(platform("com.google.firebase:firebase-bom:33.10.0"))

    implementation ("com.airbnb.android:lottie-compose:6.4.0")

    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.32.0")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation ("com.google.accompanist:accompanist-pager-indicators:0.30.1")
    implementation ("androidx.compose.foundation:foundation:1.6.0")


    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.32.0") // Check for the latest version
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}