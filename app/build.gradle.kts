plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

}

android {
    namespace = "com.towhid.swpc"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.towhid.swpc"
        minSdk = 29
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.espresso.core)

    implementation (libs.androidx.hilt.navigation.compose)
    implementation (libs.hilt.android)
    implementation("androidx.compose.material:material-icons-extended:1.7.8")
    implementation("com.airbnb.android:lottie-compose:6.0.0")

    implementation (libs.androidx.lifecycle.runtime.ktx)
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation (libs.androidx.lifecycle.viewmodel.compose)


    implementation("androidx.compose.ui:ui:1.6.0") // or latest version
    implementation("androidx.compose.material:material:1.6.0") // or latest version
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0") // or latest version
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0") //For secure storage of tokens
    implementation("com.google.code.gson:gson:2.10.1")

    implementation ("androidx.navigation:navigation-compose:2.8.8")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation ("androidx.datastore:datastore-preferences:1.1.3")


    annotationProcessor ("com.google.dagger:hilt-compiler:2.55")

    // For instrumentation tests
    androidTestImplementation  ("com.google.dagger:hilt-android-testing:2.55")
    androidTestAnnotationProcessor ("com.google.dagger:hilt-compiler:2.55")




    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}