package com.umc.halo.data.repository.relationship

import com.umc.halo.core.network.BaseResponse
import com.umc.halo.data.remote.api.relationship.RelationshipApi
import com.umc.halo.data.remote.dto.response.relationship.RelationshipTagResponse
import com.umc.halo.domain.model.relationship.RelationshipInfo
import com.umc.halo.domain.model.relationship.RelationshipTag
import com.umc.halo.domain.repository.relationship.RelationshipRepository
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

class RelationshipRepositoryImpl @Inject constructor(
    private val relationshipApi: RelationshipApi
) : RelationshipRepository {

    override suspend fun getRelationshipInfo(): RelationshipInfo {
        val response = runCatching {
            relationshipApi.getRelationshipInfo()
        }.getOrElse { throwable ->
            throw IllegalStateException(throwable.toApiErrorMessage("관계 정보를 불러오지 못했어요."))
        }
        val result = response.requireResult("관계 정보를 불러오지 못했어요.")

        return RelationshipInfo(
            parentPersonalityTags = result.parentPersonalityTags.orEmpty().mapNotNull { it.toDomain() },
            currentRelationState = result.currentRelationState?.toDomain(),
            goalRelationships = result.goalRelationships.orEmpty().mapNotNull { it.toDomain() }
        )
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

private fun RelationshipTagResponse.toDomain(): RelationshipTag? {
    val id = tagId ?: return null
    val title = title?.takeIf { it.isNotBlank() } ?: return null

    return RelationshipTag(
        id = id,
        title = title,
        description = description?.takeIf { it.isNotBlank() }
    )
}
