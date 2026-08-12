package com.umc.halo.data.repository.member

import com.umc.halo.core.datastore.TokenDataStore
import com.umc.halo.core.network.BaseResponse
import com.umc.halo.data.remote.api.member.MemberApi
import com.umc.halo.domain.model.member.MemberInfo
import com.umc.halo.domain.repository.member.MemberRepository
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

/**
 * MemberRepository 구현체
 */
class MemberRepositoryImpl @Inject constructor(
    private val memberApi: MemberApi,
    private val tokenDataStore: TokenDataStore
) : MemberRepository {

    override suspend fun getMyInfo(): MemberInfo {
        val response = runCatching {
            memberApi.getMyInfo()
        }.getOrElse { throwable ->
            throw IllegalStateException(throwable.toApiErrorMessage("내 정보를 불러오지 못했어요."))
        }
        val result = response.requireResult("내 정보를 불러오지 못했어요.")

        return MemberInfo(
            memberId = result.memberId,
            name = result.name.orEmpty(),
            gender = result.gender,
            birthDate = result.birthDate,
            provider = result.provider,
            onboardingCompleted = result.onboardingCompleted == true,
            characterImageUrl = result.characterImageUrl,
            email = result.email,
            createdAt = result.createdAt
        )
    }

    override suspend fun withdraw(): Boolean {
        val success = runCatching { memberApi.withdraw() }
            .getOrNull()
            ?.isSuccess == true

        // 서버 탈퇴가 성공했을 때만 로컬 토큰을 지움
        if (success) tokenDataStore.clear()

        return success
    }

    override suspend fun userAccess() {
        memberApi.userAccess()
    }

    private fun <T> BaseResponse<T>.requireResult(defaultMessage: String): T {
        if (!isSuccess || result == null) {
            error(toApiErrorMessage(defaultMessage))
        }

        return result
    }
}

private fun Throwable.toApiErrorMessage(defaultMessage: String): String =
    if (this is HttpException) {
        response()
            ?.errorBody()
            ?.string()
            ?.extractApiErrorMessage()
            ?: defaultMessage
    } else {
        message?.takeIf { it.isNotBlank() } ?: defaultMessage
    }

private fun BaseResponse<*>.toApiErrorMessage(defaultMessage: String): String =
    message.takeIf { it.isNotBlank() } ?: defaultMessage

private fun String.extractApiErrorMessage(): String? =
    runCatching {
        val json = JSONObject(this)
        val result = json.opt("result")
        when (result) {
            is JSONObject -> {
                val keys = result.keys()
                if (keys.hasNext()) {
                    result.optString(keys.next()).takeIf { it.isNotBlank() }
                } else {
                    null
                }
            }
            is String -> result.takeIf { it.isNotBlank() }
            else -> null
        } ?: json.optString("message").takeIf { it.isNotBlank() }
    }.getOrNull()
