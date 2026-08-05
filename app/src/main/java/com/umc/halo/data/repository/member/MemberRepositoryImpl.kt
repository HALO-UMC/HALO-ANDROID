package com.umc.halo.data.repository.member

import com.umc.halo.core.datastore.TokenDataStore
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
        val result = memberApi.getMyInfo().result
            ?: throw IllegalStateException("my info data is null")

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
}
