plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.mobdeve.s16.druzali.shawn.mco2"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.mobdeve.s16.druzali.shawn.mco2"
        minSdk = 24
        targetSdk = 36
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // UI
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.recyclerview:recyclerview:1.3.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
// Location (for prototype; real fused provider uses play-services-location)
    implementation("com.google.android.gms:play-services-location:21.0.1")
// Optional for real Google Maps in Phase 3
    implementation("com.google.android.gms:play-services-maps:18.1.0")
}

