plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.galaxywatch8.watchface"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.keingma.watch"
        minSdk = 34
        targetSdk = 34
        versionCode = 2
        versionName = "1.0"
    }

    buildTypes {
        release {
            // Watch Face Format bundles are resource-only (android:hasCode="false"),
            // so there is no code to shrink/obfuscate here.
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }
}
