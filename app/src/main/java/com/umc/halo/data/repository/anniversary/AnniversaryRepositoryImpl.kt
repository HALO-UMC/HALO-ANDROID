package com.umc.halo.data.repository.anniversary

import com.umc.halo.core.network.BaseResponse
import com.umc.halo.data.remote.api.anniversary.AnniversaryApi
import com.umc.halo.data.remote.dto.request.anniversary.AnniversarySaveRequest
import com.umc.halo.data.remote.dto.response.anniversary.AnniversaryListResponse
import com.umc.halo.domain.model.anniversary.AnniversaryOverview
import com.umc.halo.domain.model.anniversary.AnniversarySaveForm
import com.umc.halo.domain.model.anniversary.CommonAnniversary
import com.umc.halo.domain.model.anniversary.MyAnniversary
import com.umc.halo.domain.model.anniversary.UpcomingAnniversary
import com.umc.halo.domain.repository.anniversary.AnniversaryRepository
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

class AnniversaryRepositoryImpl @Inject constructor(
    private val anniversaryApi: AnniversaryApi
) : AnniversaryRepository {
    override suspend fun getAnniversaries(): AnniversaryOverview {
        val response = runCatching {
            anniversaryApi.getAnniversaries()
        }.getOrElse { throwable ->
            throw IllegalStateException(
                throwable.toApiErrorMessage("기념일 정보를 불러오지 못했어요.")
            )
        }
        val result = response.result
        if (!response.isSuccess || result == null) {
            error(response.toApiErrorMessage("기념일 정보를 불러오지 못했어요."))
        }

        return result.toDomain()
    }

    override suspend fun createAnniversary(form: AnniversarySaveForm): Long {
        val response = runCatching {
            anniversaryApi.createAnniversary(form.toRequest())
        }.getOrElse { throwable ->
            throw IllegalStateException(
                throwable.toApiErrorMessage("기념일을 저장하지 못했어요.")
            )
        }
        val result = response.result
        if (!response.isSuccess || result?.anniversaryId == null) {
            error(response.toApiErrorMessage("기념일을 저장하지 못했어요."))
        }

        return result.anniversaryId
    }

    override suspend fun updateAnniversary(anniversaryId: Long, form: AnniversarySaveForm): Long {
        val response = runCatching {
            anniversaryApi.updateAnniversary(anniversaryId, form.toRequest())
        }.getOrElse { throwable ->
            throw IllegalStateException(
                throwable.toApiErrorMessage("기념일을 저장하지 못했어요.")
            )
        }
        val result = response.result
        if (!response.isSuccess || result?.anniversaryId == null) {
            error(response.toApiErrorMessage("기념일을 저장하지 못했어요."))
        }

        return result.anniversaryId
    }

    override suspend fun deleteAnniversaries(anniversaryIds: List<Long>) {
        val response = runCatching {
            anniversaryApi.deleteAnniversaries(anniversaryIds)
        }.getOrElse { throwable ->
            throw IllegalStateException(
                throwable.toApiErrorMessage("기념일을 삭제하지 못했어요.")
            )
        }
        if (!response.isSuccess) {
            error(response.toApiErrorMessage("기념일을 삭제하지 못했어요."))
        }
    }
}

private fun AnniversaryListResponse.toDomain(): AnniversaryOverview = AnniversaryOverview(
    upcomingAnniversaries = upcomingAnniversaries.orEmpty().mapNotNull { dto ->
        val title = dto.title ?: return@mapNotNull null
        val date = dto.anniversaryDate ?: return@mapNotNull null
        UpcomingAnniversary(
            anniversaryId = dto.anniversaryId,
            commonAnniversaryId = dto.commonAnniversaryId,
            title = title,
            anniversaryDate = date,
            dDay = dto.dDay ?: 0
        )
    },
    myAnniversaries = myAnniversaries.orEmpty().mapNotNull { dto ->
        MyAnniversary(
            anniversaryId = dto.anniversaryId ?: return@mapNotNull null,
            title = dto.title.orEmpty(),
            anniversaryDate = dto.anniversaryDate ?: return@mapNotNull null,
            displayDate = dto.displayDate ?: dto.upcomingDate ?: dto.solarDate,
            isLunar = dto.isLunar == true,
            isRepeated = dto.isRepeated == true,
            sevenDaysAlarmEnabled = dto.sevenDaysAlarmEnabled == true,
            dayAlarmEnabled = dto.dayAlarmEnabled == true,
            memo = dto.memo
        )
    },
    commonAnniversaries = commonAnniversaries.orEmpty().mapNotNull { dto ->
        CommonAnniversary(
            commonAnniversaryId = dto.commonAnniversaryId ?: return@mapNotNull null,
            title = dto.title.orEmpty(),
            month = dto.month ?: return@mapNotNull null,
            day = dto.day ?: return@mapNotNull null,
            isLunar = dto.isLunar == true,
            sevenDaysAlarmEnabled = dto.sevenDaysAlarmEnabled == true,
            dayAlarmEnabled = dto.dayAlarmEnabled == true,
            memo = dto.memo
        )
    }
)

private fun AnniversarySaveForm.toRequest(): AnniversarySaveRequest = AnniversarySaveRequest(
    title = title,
    anniversaryDate = anniversaryDate,
    isLunar = isLunar,
    isRepeated = isRepeated,
    sevenDaysAlarmEnabled = sevenDaysAlarmEnabled,
    dayAlarmEnabled = dayAlarmEnabled,
    memo = memo?.takeIf { it.isNotBlank() }
)

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
