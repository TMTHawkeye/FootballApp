plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")  // Add this line

}

android {
    namespace = "com.example.footballapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.footballapp"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(project(":footballapi"))


    implementation ("com.intuit.sdp:sdp-android:1.1.1")
    implementation ("com.intuit.ssp:ssp-android:1.1.1")

    implementation("com.tbuonomo:dotsindicator:4.3")

    implementation ("com.github.bumptech.glide:glide:4.15.1")

    implementation ("com.github.Dimezis:BlurView:version-2.0.3")

    implementation ("me.relex:circleindicator:2.1.6")

    implementation ("com.google.android.exoplayer:exoplayer:2.18.1")


    implementation ("androidx.fragment:fragment-ktx:1.6.2")

    implementation("io.insert-koin:koin-android:3.5.6")



    implementation("androidx.paging:paging-runtime-ktx:3.3.2")


    implementation("com.facebook.shimmer:shimmer:0.5.0")


    implementation("org.jetbrains.kotlin:kotlin-reflect: 1.9.0")

    implementation("com.google.code.gson:gson:2.11.0")


     implementation("com.github.AliAsadi:PowerPreference:2.1.1")



}