package com.umc.halo.presentation.component

import androidx.annotation.DrawableRes
import com.umc.halo.R

/**
 * 서버 storybookId → 책등 그림 매핑
 *
 */
private val SpineByStorybookId: Map<Long, Int> = mapOf(
    1L to R.drawable.image_common_bookcase_1,    // 오래전 당신
    2L to R.drawable.image_common_bookcase_2,    // 당신 사용설명서
    3L to R.drawable.image_common_bookcase_3,   // 가족의 온도
    4L to R.drawable.image_common_bookcase_4,    // 취향이 닿는 날
    5L to R.drawable.image_common_bookcase_5,    // 나란히 걷는 날
    6L to R.drawable.image_common_bookcase_6,    // 오늘은 내가 먼저
    7L to R.drawable.image_common_bookcase_7,    // 생신까지 열 장
    8L to R.drawable.image_common_bookcase_8,    // 한 장의 가족사진
    9L to R.drawable.image_common_bookcase_9,    // 손을 내미는 연습
    10L to R.drawable.image_common_bookcase_10    // 당신의 1호 팬
)

/** 모르는 id 일 때 대신 그릴 책등  */
private val FallbackSpine = R.drawable.image_common_bookcase_1

/**
 * [storybookId] 에 해당하는 책등 그림
 *
 */
@DrawableRes
fun storybookSpineOf(storybookId: Long): Int =
    SpineByStorybookId[storybookId] ?: FallbackSpine
