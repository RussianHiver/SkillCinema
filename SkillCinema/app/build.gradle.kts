import org.jetbrains.kotlin.gradle.dsl.JvmTarget


plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.skillcinema"
    compileSdk = 37




    defaultConfig {
        applicationId = "com.example.skillcinema"
        minSdk = 27
        targetSdk = 34
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

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    val navVersion = "2.9.8"
    val lifecycleVersion = "2.11.0"
    val daggerVersion = "2.60.1"
    val glideVersion = "5.0.9"


    implementation("androidx.core:core-ktx:1.19.0")
    implementation("androidx.appcompat:appcompat:1.8.0")
    implementation("com.google.android.material:material:1.14.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")

    implementation("androidx.viewpager2:viewpager2:1.1.0")
    implementation("me.relex:circleindicator:2.1.6")

    //Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    //Viewmodel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")

    //Dagger
    implementation("com.google.dagger:dagger:$daggerVersion")
    ksp ("com.google.dagger:dagger-compiler:$daggerVersion")

    //Room
    implementation("androidx.room:room-runtime:2.8.4")
    implementation("androidx.room:room-ktx:2.8.4")
    ksp("androidx.room:room-compiler:2.8.4")

    //RecycleView
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation("androidx.recyclerview:recyclerview-selection:1.2.0")

    //Paging
    implementation("androidx.paging:paging-runtime-ktx:3.5.1")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:3.0.0")

    //Moshi
    implementation("com.squareup.moshi:moshi:1.15.2")
    implementation("com.squareup.retrofit2:converter-moshi:3.0.0")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:1.15.2")

    //Glide
    implementation("com.github.bumptech.glide:glide:$glideVersion")
    ksp("com.github.bumptech.glide:glide:$glideVersion")

}