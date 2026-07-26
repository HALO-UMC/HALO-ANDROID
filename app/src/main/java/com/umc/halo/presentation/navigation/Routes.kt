package com.umc.halo.presentation.navigation

object Routes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val LOGIN = "login"

    // Bottom Navigation
    const val HOME = "home"
    const val CALENDAR = "calendar"
    const val THEME_BOX = "theme_box"
    const val STORYBOOK = "storybook"
    const val MYPAGE = "mypage"

    // MyPage inner screens
    const val MYPAGE_RELATIONSHIP_INFO = "mypage_relationship_info"
    const val MYPAGE_ANNIVERSARY = "mypage_anniversary"
    const val MYPAGE_SYSTEM_SETTINGS = "mypage_system_settings"
    const val MYPAGE_NOTIFICATION_SETTINGS = "mypage_notification_settings"
    const val MYPAGE_ACCOUNT_MANAGEMENT = "mypage_account_management"
    const val MYPAGE_ACCOUNT_INFO = "mypage_account_info"
    const val MYPAGE_WITHDRAW = "mypage_withdraw"
    const val MYPAGE_TERMS = "mypage_terms"
    const val MYPAGE_OPEN_LICENSE = "mypage_open_license"
    const val MYPAGE_PRIVACY_POLICY = "mypage_privacy_policy"

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
