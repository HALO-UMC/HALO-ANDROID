package com.umc.halo.data.repository.member

import com.umc.halo.core.datastore.TokenDataStore
import com.umc.halo.data.remote.api.member.MemberApi
import com.umc.halo.domain.repository.member.MemberRepository
import javax.inject.Inject

/**
 * MemberRepository 구현체
 */
class MemberRepositoryImpl @Inject constructor(
    private val memberApi: MemberApi,
    private val tokenDataStore: TokenDataStore
) : MemberRepository {

    override suspend fun withdraw(): Boolean {
        val success = runCatching { memberApi.withdraw() }
            .getOrNull()
            ?.isSuccess == true

        // 서버 탈퇴가 성공했을 때만 로컬 토큰을 지움
        if (success) tokenDataStore.clear()

        return success
    }
}
