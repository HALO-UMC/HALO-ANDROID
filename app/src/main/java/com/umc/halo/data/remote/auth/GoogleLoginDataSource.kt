package com.umc.halo.data.remote.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.umc.halo.BuildConfig
import javax.inject.Inject

/**
 * 구글 로그인 호출을 코루틴으로 함
 * 계정 선택 UI 를 띄우고 성공하면 OIDC idToken 을 반환
 */
class GoogleLoginDataSource @Inject constructor() {

    /**
     * @param context 계정 선택 UI 를 띄울 Activity Context
     * @return 구글 OIDC idToken
     * @throws Throwable 로그인 실패/취소 또는 idToken이 없을 떄
     */
    suspend fun login(context: Context): String {
        val credentialManager = CredentialManager.create(context)

        val signInOption = GetSignInWithGoogleOption
            .Builder(serverClientId = BuildConfig.GOOGLE_WEB_CLIENT_ID)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(signInOption)
            .build()

        // 계정 선택 UI 표시
        val response = credentialManager.getCredential(context = context, request = request)

        val credential = response.credential
        if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            // 자격증명에서 OIDC idToken 추출
            return GoogleIdTokenCredential.createFrom(credential.data).idToken
        }

        error("구글 로그인 실패: 알 수 없는 자격증명 타입(${credential.type})")
    }
}
