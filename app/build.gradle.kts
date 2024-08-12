plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.quiz.prize"
    compileSdk = 34

    defaultConfig {
        vectorDrawables {
            useSupportLibrary = true
        }
        applicationId = "com.quiz.prize"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
                "proguard-rules-new.pro"
            )
        }
    }
    sourceSets {
        getByName("main") {
            assets.srcDirs("src/main/assets", "src/main/assets/")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
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
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("com.android.volley:volley:1.2.1")

    implementation(platform("com.google.firebase:firebase-bom:30.4.1"))
    implementation("com.google.firebase:firebase-core")
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-storage")
    implementation("com.facebook.android:facebook-android-sdk:11.3.0")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.hbb20:ccp:2.5.4")
    implementation("com.google.android.gms:play-services-auth:21.0.0")
    implementation("com.google.android.play:core:1.10.3")
    implementation("com.github.CanHub:Android-Image-Cropper:3.1.2")
    implementation("com.facebook.shimmer:shimmer:0.5.0@aar")
    implementation("com.android.billingclient:billing:6.2.0")
    implementation("androidx.preference:preference:1.1.1")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")
    implementation("com.google.android.gms:play-services-ads:23.0.0")
    implementation("org.jetbrains:annotations:15.0")
    implementation("com.google.ads.mediation:facebook:6.16.0.0")
    implementation("com.airbnb.android:lottie:3.6.1")

    val lifecycleVersion = "2.0.0"
    implementation("androidx.lifecycle:lifecycle-extensions:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime:$lifecycleVersion")
    annotationProcessor("androidx.lifecycle:lifecycle-compiler:$lifecycleVersion")

    implementation("io.jsonwebtoken:jjwt:0.9.1")

    androidTestImplementation("com.android.support.test.espresso:espresso-core:2.2.2") {
        exclude(group = "com.android.support", module = "support-annotations")
    }
}