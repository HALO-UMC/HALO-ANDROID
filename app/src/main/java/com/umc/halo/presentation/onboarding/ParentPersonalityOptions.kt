package com.umc.halo.presentation.onboarding

/**
 * 부모님 성격 태그는 모든 카테고리를 합쳐
 * 최소 1개부터 최대 3개까지 선택할 수 있다.
 */
internal data class ParentPersonalityGroup(
    val title: String,
    val options: List<String>
)

internal val PARENT_PERSONALITY_GROUPS = listOf(
    ParentPersonalityGroup(
        title = "긍정적인 성향",
        options = listOf(
            "낙천적인",
            "배려심 깊은",
            "열정적인",
            "온화한",
            "유머러스한",
            "솔직한"
        )
    ),
    ParentPersonalityGroup(
        title = "중립적인 성향",
        options = listOf(
            "활동적인",
            "차분한",
            "계획적인",
            "즉흥적인",
            "감성적인",
            "이성적인"
        )
    ),
    ParentPersonalityGroup(
        title = "신중하게 보는 성향",
        options = listOf(
            "걱정 많은",
            "잔소리",
            "고집 있는",
            "소극적인",
            "권위적인",
            "비판적인"
        )
    )
)
