package com.umc.halo.presentation.storybook.chapter

/**
 * 하나의 챕터를 작성하는 내부 단계
 *
 * 각 단계를 별도의 Navigation Route로 만들지 않고,
 * ChapterProgressScreen 안에서 currentStep만 변경합니다.
 */
enum class ChapterProgressStep(
    val displayName: String
) {
    INTRO("챕터 소개"),
    GUIDE("챕터 안내"),
    QUESTION("기억 기록"),
    SCENE("장면 선택"),
    SCENE_CONFIRM("장면 확인"),
    MOOD("감정 기록"),
    REVIEW("최종 확인");

    fun next(): ChapterProgressStep {
        return when (this) {
            INTRO -> GUIDE
            GUIDE -> QUESTION
            QUESTION -> SCENE
            SCENE -> SCENE_CONFIRM
            SCENE_CONFIRM -> MOOD
            MOOD -> REVIEW
            REVIEW -> REVIEW
        }
    }

    fun previous(): ChapterProgressStep {
        return when (this) {
            INTRO -> INTRO
            GUIDE -> INTRO
            QUESTION -> GUIDE
            SCENE -> QUESTION
            SCENE_CONFIRM -> SCENE
            MOOD -> SCENE_CONFIRM
            REVIEW -> MOOD
        }
    }
}