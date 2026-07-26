package com.umc.halo.presentation.navigation

object Routes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val LOGIN = "login"
    const val TERMS = "terms"

    // Bottom Navigation
    const val HOME = "home"
    const val CALENDAR = "calendar"
    const val THEME_BOX = "theme_box"
    const val STORYBOOK = "storybook"
    const val MYPAGE = "mypage"

    // Storybook inner screens
    const val STORYBOOK_DETAIL = "storybook_detail/{storybookId}"

    // Chapter screens
    const val CHAPTER_PROGRESS = "chapter_progress/{storybookId}/{chapterId}"
    const val CHAPTER_RESULT = "chapter_result/{storybookId}/{chapterId}"

    fun storybookDetail(storybookId: Long): String =
        "storybook_detail/$storybookId"

    fun chapterProgress(
        storybookId: Long,
        chapterId: Long
    ): String =
        "chapter_progress/$storybookId/$chapterId"

    fun chapterResult(
        storybookId: Long,
        chapterId: Long
    ): String =
        "chapter_result/$storybookId/$chapterId"
}