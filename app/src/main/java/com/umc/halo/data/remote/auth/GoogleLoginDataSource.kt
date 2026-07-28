package com.umc.halo.data.remote.auth

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.umc.halo.BuildConfig
import com.umc.halo.domain.model.auth.LoginCancelledException
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
        // 사용자가 계정 선택 창을 닫으면 취소 예외가 오는데 카카오와 같은 예외로 통일해 던짐
        val response = try {
            credentialManager.getCredential(context = context, request = request)
        } catch (e: GetCredentialCancellationException) {
            throw LoginCancelledException(e)
        }

        val credential = response.credential
        if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            // 자격증명에서 OIDC idToken 추출
            return GoogleIdTokenCredential.createFrom(credential.data).idToken
        }

        error("구글 로그인 실패: 알 수 없는 자격증명 타입(${credential.type})")
    }

    /**
     * 기기에 남아있는 구글 자격증명 선택 기록을 지움 (회원 탈퇴 시 호출)
     *
     * 지우지 않으면 재가입할 때 계정 선택 창 없이 직전 계정으로 바로 붙움
     */
    suspend fun clearCredentialState(context: Context) {
        runCatching {
            CredentialManager.create(context)
                .clearCredentialState(ClearCredentialStateRequest())
        }
    }
}
