plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.application"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.application"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "API_KEY", "\"a2ae691b53msh393e153de705864p186a6cjsnbdf23b779fc9\"")
            buildConfigField("String", "API_HOST", "\"spoonacular-recipe-food-nutrition-v1.p.rapidapi.com\"")
            buildConfigField("String", "XAPI_KEY", "\"kLB2Kaq64UizpgnzYYxoiQ==fZSjgLIWhOmKqByu\"")
        }
        debug {
            buildConfigField("String", "API_KEY", "\"a2ae691b53msh393e153de705864p186a6cjsnbdf23b779fc9\"")
            buildConfigField("String", "API_HOST", "\"spoonacular-recipe-food-nutrition-v1.p.rapidapi.com\"")
            buildConfigField("String", "XAPI_KEY", "\"kLB2Kaq64UizpgnzYYxoiQ==fZSjgLIWhOmKqByu\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.squareup.okio:okio:2.10.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation(libs.androidx.recyclerview)
    implementation("com.squareup.picasso:picasso:2.71828")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}