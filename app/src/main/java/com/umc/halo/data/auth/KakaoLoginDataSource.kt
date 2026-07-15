package com.umc.halo.data.auth

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * 카카오 로그인 SDK 호출을 코루틴으로 함
 * 카카오톡 앱이 있으면 앱으로 없으면 카카오계정(웹)으로 로그인
 * 성공하면 OIDC(idToken)을 반환
 */
class KakaoLoginDataSource @Inject constructor() {

    /**
     * @return 카카오 OIDC idToken
     * @throws Throwable 로그인 실패/취소 또는 idToken 이 없을 때
     */
    suspend fun login(context: Context): String = suspendCancellableCoroutine { cont ->
        // 로그인 결과 공통 처리: 성공 시 idToken 을 코루틴 결과로 반환
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            when {
                error != null -> cont.resumeWithException(error)
                token?.idToken != null -> cont.resume(token.idToken!!)
                else -> cont.resumeWithException(
                    IllegalStateException("오류: idToken 이 null임! OpenID Connect 활성화 여부 확인 필요.")
                )
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            // 카카오톡 앱으로 로그인
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                when {
                    // 사용자가 카카오톡 로그인 화면에서 직접 취소한 경우 실패 처리
                    error is ClientError && error.reason == ClientErrorCause.Cancelled ->
                        cont.resumeWithException(error)

                    // 그 외 오류면 카카오계정(웹) 로그인으로 fallback
                    error != null ->
                        UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)

                    else -> callback(token, null)
                }
            }
        } else {
            // 카카오계정(웹)으로 로그인
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }
}
