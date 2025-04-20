plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    id("com.google.gms.google-services")

    id("kotlin-parcelize") // Add this line

    id("kotlin-kapt")

    id("androidx.navigation.safeargs")


}

val googleClientId: String = project.findProperty("GOOGLE_CLIENT_ID") as String

android {
    namespace = "com.example.shoppy_onlineshop"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.shoppy_onlineshop"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "GOOGLE_CLIENT_ID", "\"${googleClientId}\"")

    }

    signingConfigs {
        named("debug") {
            storeFile = file("C:/Users/veronika/.android/debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }

    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
        }
        getByName("release") {
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
        viewBinding = true
        buildConfig = true
    }
}



dependencies {

    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.play.services.analytics.impl)
    implementation(libs.androidx.media3.common.ktx)
    implementation(libs.firebase.common.ktx)
    implementation(libs.firebase.database.ktx)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.androidx.room.runtime.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.material.v190)


    // Glide dependency
    implementation(libs.glide)

    kapt(libs.compiler)

    //HTTP Dependency
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    implementation(libs.kotlinx.coroutines.android)

    //Gson dependency
    implementation(libs.converter.gson)

    implementation(libs.material)

    //Firebase auth dependency - handles user authentication
    implementation(libs.firebase.auth)
    //For storing user credentials automatically
    implementation(libs.androidx.datastore.preferences)


    implementation(libs.firebase.auth.v2230)
    implementation(libs.play.services.auth.v2100)


    implementation(libs.androidx.recyclerview)

    //facebook shimmer
    implementation(libs.shimmer)

    //Google Sign In
    implementation(libs.play.services.auth)
    implementation(libs.firebase.auth.ktx.v2230)

    // CameraX core libraries
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.extensions)

    //ML
    implementation(libs.image.labeling)

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx.v262)
    implementation(libs.androidx.fragment.ktx.v162)








}