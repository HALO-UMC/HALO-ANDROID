package com.umc.halo.presentation.navigation

object Routes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val LOGIN = "login"
    const val TERMS = "terms"

    // Bottom Navigation
    const val HOME = "home"
    const val CALENDAR = "calendar"
    // 테마함에서 사용하는 스토리북ID (선택 인자)
    const val THEME_BOX = "theme_box?storybookId={storybookId}"
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

    // 테마 감상하기
    const val SHOW_THEME = "show_theme/{storybookId}"

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

    fun showTheme(
        storybookId: Long
    ): String =
        "show_theme/$storybookId"

    /**
     * 테마함 이동 경로.
     * @param storybookId 지정하면 그 스토리북의 테마부터 보여준다. null 이면 기본 위치(첫 번째 테마).
     */
    fun themeBox(storybookId: Long? = null): String =
        if (storybookId == null) "theme_box" else "theme_box?storybookId=$storybookId"

    /** 테마함 진입 시 '지정 없음'을 뜻하는 storybookId 기본값 */
    const val NO_STORYBOOK_ID = -1L
}
