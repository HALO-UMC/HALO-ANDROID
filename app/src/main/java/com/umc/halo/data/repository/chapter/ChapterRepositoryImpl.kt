package com.umc.halo.data.repository.chapter

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.umc.halo.core.network.BaseResponse
import com.umc.halo.data.remote.api.chapter.ChapterApi
import com.umc.halo.data.remote.dto.request.chapter.ChapterAnswerRequest
import com.umc.halo.data.remote.dto.request.chapter.ChapterSaveRequest
import com.umc.halo.data.remote.dto.request.chapter.PresignedUrlRequest
import com.umc.halo.data.remote.dto.response.chapter.ChapterDraftResponse
import com.umc.halo.data.remote.dto.response.chapter.CompletedChapterResponse
import com.umc.halo.data.remote.dto.response.chapter.TodayChapterResponse
import com.umc.halo.domain.model.storybook.Chapter
import com.umc.halo.domain.model.storybook.ChapterCoverType
import com.umc.halo.domain.model.storybook.ChapterDraft
import com.umc.halo.domain.model.storybook.ChapterDraftAnswer
import com.umc.halo.domain.model.storybook.ChapterDraftStatus
import com.umc.halo.domain.model.storybook.ChapterEmotion
import com.umc.halo.domain.model.storybook.ChapterQuestion
import com.umc.halo.domain.model.storybook.ChapterSaveForm
import com.umc.halo.domain.model.storybook.ChapterSaveResult
import com.umc.halo.domain.model.storybook.ChapterSaveStatus
import com.umc.halo.domain.model.storybook.ChapterSceneCard
import com.umc.halo.domain.model.storybook.ChapterStatus
import com.umc.halo.domain.model.storybook.CompletedChapter
import com.umc.halo.domain.model.storybook.CompletedChapterAnswer
import com.umc.halo.domain.repository.chapter.ChapterRepository
import com.umc.halo.domain.repository.chapter.TodayChapter
import com.umc.halo.domain.repository.chapter.UploadedChapterImage
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

class ChapterRepositoryImpl @Inject constructor(
    private val chapterApi: ChapterApi,
    @ApplicationContext private val context: Context
) : ChapterRepository {
    private val s3Client = OkHttpClient.Builder().build()

    override suspend fun getTodayChapter(
        storybookId: Long,
        chapterOrder: Int
    ): TodayChapter {
        val response = runCatching {
            chapterApi.getTodayChapter(storybookId, chapterOrder)
        }.getOrElse { throwable ->
            throw IllegalStateException(throwable.toApiErrorMessage("오늘의 장을 불러오지 못했어요."))
        }

        val result = response.result
        if (!response.isSuccess || result == null) {
            error(response.toApiErrorMessage("오늘의 장을 불러오지 못했어요."))
        }

        return result.toDomain(storybookId)
    }

    override suspend fun saveMemberChapter(form: ChapterSaveForm): ChapterSaveResult {
        val response = runCatching {
            chapterApi.saveMemberChapter(form.toRequest())
        }.getOrElse { throwable ->
            throw IllegalStateException(throwable.toApiErrorMessage("장 기록을 저장하지 못했어요."))
        }

        val result = response.result
        if (!response.isSuccess || result?.memberChapterId == null) {
            error(response.toApiErrorMessage("장 기록을 저장하지 못했어요."))
        }

        return ChapterSaveResult(
            memberChapterId = result.memberChapterId,
            isStorybookCompleted = result.isStorybookCompleted == true
        )
    }

    override suspend fun getCompletedChapter(memberChapterId: Long): CompletedChapter {
        val response = runCatching {
            chapterApi.getCompletedChapter(memberChapterId)
        }.getOrElse { throwable ->
            throw IllegalStateException(throwable.toApiErrorMessage("완료된 장을 불러오지 못했어요."))
        }

        val result = response.result
        if (!response.isSuccess || result == null) {
            error(response.toApiErrorMessage("완료된 장을 불러오지 못했어요."))
        }

        return result.toDomain()
    }

    override suspend fun uploadImageFromUri(imageUri: String): UploadedChapterImage {
        val uri = Uri.parse(imageUri)
        val contentType = context.contentResolver.getType(uri)
            ?: error("지원하지 않는 이미지 형식입니다.")
        val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            ?: error("사진을 읽지 못했어요.")
        val fileSize = queryFileSize(uri) ?: bytes.size.toLong()

        val presignedResponse = runCatching {
            chapterApi.createPresignedUrl(
                PresignedUrlRequest(
                    contentType = contentType,
                    fileSize = fileSize
                )
            )
        }.getOrElse { throwable ->
            throw IllegalStateException(throwable.toApiErrorMessage("사진 업로드에 실패했어요. 다시 선택해주세요."))
        }

        val presigned = presignedResponse.result
        if (
            !presignedResponse.isSuccess ||
            presigned?.presignedUrl.isNullOrBlank() ||
            presigned?.imageKey.isNullOrBlank()
        ) {
            error(presignedResponse.toApiErrorMessage("사진 업로드에 실패했어요. 다시 선택해주세요."))
        }

        val request = Request.Builder()
            .url(presigned!!.presignedUrl!!)
            .put(bytes.toRequestBody(contentType.toMediaType()))
            .header("Content-Type", contentType)
            .build()

        s3Client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                error("사진 업로드에 실패했어요. 다시 선택해주세요.")
            }
        }

        return UploadedChapterImage(imageKey = presigned.imageKey!!)
    }

    private fun queryFileSize(uri: Uri): Long? =
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            if (sizeIndex >= 0 && cursor.moveToFirst()) {
                cursor.getLong(sizeIndex).takeIf { it > 0 }
            } else {
                null
            }
        }
}

private fun TodayChapterResponse.toDomain(storybookId: Long): TodayChapter {
    val id = chapterId ?: error("장 ID가 없습니다.")
    val order = chapterOrder ?: error("장 순서가 없습니다.")
    val questionModels = questions.orEmpty()
        .mapNotNull { dto ->
            ChapterQuestion(
                id = dto.chapterQuestionId ?: return@mapNotNull null,
                order = dto.questionOrder ?: 0,
                question = dto.question.orEmpty()
            )
        }
        .sortedBy { it.order }

    val sceneCardModels = sceneCards.orEmpty()
        .mapNotNull { dto ->
            ChapterSceneCard(
                id = dto.sceneCardId ?: return@mapNotNull null,
                storybookId = storybookId,
                chapterId = id,
                title = "",
                imageUrl = dto.imageUrl
            )
        }

    val chapter = Chapter(
        id = id,
        storybookId = storybookId,
        storybookTitle = storybookTitle.orEmpty(),
        number = order,
        title = chapterTitle.orEmpty(),
        description = description.orEmpty(),
        backgroundImageUrl = longImageUrl,
        guideImageUrl = character?.writingCharacterImageUrl,
        characterImageUrl = character?.sceneCardCharacterImageUrl,
        themeGuideText = "",
        chapterGuideText = guide.orEmpty(),
        questions = questionModels,
        status = ChapterStatus.AVAILABLE
    )

    return TodayChapter(
        chapter = chapter,
        sceneCards = sceneCardModels,
        draft = draft.toDomain()
    )
}

private fun ChapterDraftResponse?.toDomain(): ChapterDraft =
    ChapterDraft(
        status = ChapterDraftStatus.from(this?.status),
        answers = this?.answers.orEmpty()
            .mapNotNull { dto ->
                ChapterDraftAnswer(
                    chapterQuestionId = dto.chapterQuestionId ?: return@mapNotNull null,
                    questionOrder = dto.questionOrder ?: 0,
                    answer = dto.answer.orEmpty()
                )
            }
            .sortedBy { it.questionOrder },
        coverType = ChapterCoverType.from(this?.coverType),
        imageUrl = this?.imageUrl,
        imageKey = this?.imageKey,
        sceneCardId = this?.sceneCardId,
        emotion = ChapterEmotion.from(this?.emotion)
    )

private fun ChapterSaveForm.toRequest(): ChapterSaveRequest =
    ChapterSaveRequest(
        chapterId = chapterId,
        emotion = emotion?.name,
        coverType = coverType?.name,
        imageKey = imageKey,
        sceneCardId = sceneCardId,
        answers = answers.map {
            ChapterAnswerRequest(
                chapterQuestionId = it.chapterQuestionId,
                answer = it.answer
            )
        },
        status = status.name
    )

private fun CompletedChapterResponse.toDomain(): CompletedChapter =
    CompletedChapter(
        memberChapterId = memberChapterId ?: error("완료 장 ID가 없습니다."),
        storybookTitle = storybookTitle.orEmpty(),
        chapterTitle = chapterTitle.orEmpty(),
        chapterOrder = chapterOrder ?: 0,
        description = description.orEmpty(),
        emotion = ChapterEmotion.from(emotion) ?: ChapterEmotion.GRATEFUL,
        coverType = ChapterCoverType.from(coverType) ?: ChapterCoverType.IMAGE,
        imageUrl = imageUrl,
        sceneCardImageUrl = sceneCardImageUrl,
        answers = answers.orEmpty().map {
            CompletedChapterAnswer(
                question = it.question.orEmpty(),
                answer = it.answer.orEmpty()
            )
        },
        completedDate = completedDate.orEmpty(),
        chapterImageUrl = chapterImageUrl.orEmpty()
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
        val fieldErrors = json.optJSONObject("result")
        if (fieldErrors != null) {
            val keys = fieldErrors.keys()
            if (keys.hasNext()) {
                fieldErrors.optString(keys.next()).takeIf { it.isNotBlank() }
            } else {
                null
            }
        } else {
            json.optString("message").takeIf { it.isNotBlank() }
        }
    }.getOrNull()
