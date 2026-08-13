package com.umc.halo.data.repository.member

import com.umc.halo.core.datastore.TokenDataStore
import com.umc.halo.core.network.BaseResponse
import com.umc.halo.core.network.toApiErrorMessage
import com.umc.halo.data.remote.api.member.MemberApi
import com.umc.halo.domain.model.member.MemberInfo
import com.umc.halo.domain.repository.member.MemberRepository
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

