package com.umc.halo.data.remote.api.chapter

import com.umc.halo.core.network.BaseResponse
import com.umc.halo.data.remote.dto.request.chapter.ChapterSaveRequest
import com.umc.halo.data.remote.dto.request.chapter.PresignedUrlRequest
import com.umc.halo.data.remote.dto.response.chapter.ChapterSaveResponse
import com.umc.halo.data.remote.dto.response.chapter.CompletedChapterResponse
import com.umc.halo.data.remote.dto.response.chapter.PresignedUrlResponse
import com.umc.halo.data.remote.dto.response.chapter.TodayChapterResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChapterApi {
    @GET("api/v1/storybooks/{storybookId}/chapters/{chapterOrder}")
    suspend fun getTodayChapter(
        @Path("storybookId") storybookId: Long,
        @Path("chapterOrder") chapterOrder: Int
    ): BaseResponse<TodayChapterResponse>

    @POST("api/v1/member-chapters")
    suspend fun saveMemberChapter(
        @Body request: ChapterSaveRequest
    ): BaseResponse<ChapterSaveResponse>

    @GET("api/v1/member-chapters/{memberChapterId}")
    suspend fun getCompletedChapter(
        @Path("memberChapterId") memberChapterId: Long
    ): BaseResponse<CompletedChapterResponse>

    @POST("api/v1/images/presigned-url")
    suspend fun createPresignedUrl(
        @Body request: PresignedUrlRequest
    ): BaseResponse<PresignedUrlResponse>
}
