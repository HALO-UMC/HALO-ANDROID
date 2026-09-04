import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

// 민감정보는 git에 올리지 않는 local.properties 에서 읽어옴
val localProperties = Properties().apply {
    val localFile = rootProject.file("local.properties")
    if (localFile.exists()) {
        localFile.inputStream().use { load(it) }
    }
}
// 카카오 네이티브 앱 키 (KAKAO_NATIVE_APP_KEY에 오류나면 빌드는 성공하지만 로그인은 동작 하지 않음)
val kakaoNativeAppKey: String = localProperties.getProperty("KAKAO_NATIVE_APP_KEY") ?: ""
// 구글 Web 클라이언트 ID (idToken 발급용 serverClientId) 값 없으면 빌드는 되지만 구글 로그인은 동작 안 함
val googleWebClientId: String = localProperties.getProperty("GOOGLE_WEB_CLIENT_ID") ?: ""

android {
    namespace = "com.umc.halo"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.umc.halo"
        minSdk = 24
        targetSdk = 36
        versionCode = 3
        versionName = "1.0.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // 카카오 SDK 초기화(KakaoSdk.init)에서 BuildConfig.KAKAO_NATIVE_APP_KEY 로 사용
        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", "\"$kakaoNativeAppKey\"")
        // manifest의 로그인 redirect scheme에 주입
        manifestPlaceholders["KAKAO_NATIVE_APP_KEY"] = kakaoNativeAppKey

        // 구글 로그인 GetSignInWithGoogleOption 의 serverClientId 로 사용 (BuildConfig.GOOGLE_WEB_CLIENT_ID)
        buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", "\"$googleWebClientId\"")
    }

    val keystorePath: String? = localProperties.getProperty("KEYSTORE_FILE")

    signingConfigs {
        create("release") {
            if (keystorePath != null) {
                storeFile = file(keystorePath)
                storePassword = localProperties.getProperty("KEYSTORE_PASSWORD")
                keyAlias = localProperties.getProperty("KEY_ALIAS")
                keyPassword = localProperties.getProperty("KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        debug {
            // BuildConfig.BASE_URL 로 코드에서 접근 — 개발 서버
            buildConfigField("String", "BASE_URL", "\"https://dev.halo-app.co.kr/\"")
        }
        release {
            signingConfig = signingConfigs.getByName("release")

            // BuildConfig.BASE_URL 로 코드에서 접근 — 운영 서버
            buildConfigField("String", "BASE_URL", "\"https://halo-app.co.kr/\"")

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
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.splashscreen)

    // Animation - Lottie
    implementation(libs.lottie.compose)
    implementation(libs.dotlottie)

    // DI - Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Network - Retrofit / OkHttp
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    // Local storage - DataStore
    implementation(libs.androidx.datastore.preferences)

    // 소셜 로그인 - Kakao
    implementation(libs.kakao.user)

    // 소셜 로그인 - Google (Credential Manager + Sign in with Google)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    // 푸시 알림 - FireBase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)

    // 로그 전송 - FireBase
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

    // 이미지 로드 - Coil
    implementation(libs.coil.compose)

    // Media - ExoPlayer
    implementation(libs.androidx.media3.exoplayer)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
