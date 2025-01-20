plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.exner.tools.kjsbikemaintenancechecker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.exner.tools.kjsbikemaintenancechecker"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            vcsInfo.include = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            isDebuggable = true
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    buildToolsVersion = "35.0.0"
}

kotlin {
    jvmToolchain(17)

    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
    sourceSets.test {
        kotlin.srcDir("build/generated/ksp/test/kotlin")
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

    // more
    implementation(libs.androidx.datastore.preferences)

    // hilt
    implementation(libs.hilt.android)
    implementation(libs.material)
    ksp(libs.dagger.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // room
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)
    androidTestImplementation(libs.room.testing)
    androidTestImplementation(libs.androidx.junit.ktx)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.androidx.rules)

    // compose destinations
    implementation(libs.compose.destinations.core)
    ksp(libs.compose.destinations.ksp)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

hilt {
    enableAggregatingTask = true
}

ksp {
    arg("room.schemaLocation", "$projectDir/src/main/assets/schemas")
    arg("room.incremental"   , "true")
}
