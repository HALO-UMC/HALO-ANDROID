package com.umc.halo.presentation.onboarding

data class OnboardingSelectOption(
    val title: String,
    val description: String? = null
)

object OnboardingOptions {

    val positivePersonalities = listOf(
        "낙천적인",
        "배려심 깊은",
        "열정적인",
        "온화한",
        "유머러스한",
        "솔직한"
    )

    val neutralPersonalities = listOf(
        "활동적인",
        "차분한",
        "계획적인",
        "즉흥적인",
        "감성적인",
        "이성적인"
    )

    val cautiousPersonalities = listOf(
        "걱정 많은",
        "잔소리",
        "고집 있는",
        "소극적인",
        "권위적인",
        "비판적인"
    )

    val relationshipOptions = listOf(
        OnboardingSelectOption(
            title = "매우 가까운 편이에요",
            description = "부모님이 전엔 친구 같은, 비밀까지 나누는 사이"
        ),
        OnboardingSelectOption(
            title = "대체로 좋은 편이에요",
            description = "일상적인 안부를 나누며 서로를 존중해요"
        ),
        OnboardingSelectOption(
            title = "보통이에요",
            description = "가끔 어색하지만 필요한 이야기는 나눠요"
        ),
        OnboardingSelectOption(
            title = "서먹한 편이에요",
            description = "대화가 적고 조금 거리감이 느껴질 때가 있어요"
        ),
        OnboardingSelectOption(
            title = "멀어진 것 같아요",
            description = "최근에 갈등이 있거나 거의 연락하지 않아요"
        )
    )

    val goalOptions = listOf(
        "부모님을 더 알고 싶어요",
        "같이 보내는 시간을 만들고 싶어요",
        "어색하지 않게 이야기하고 싶어요",
        "마음을 표현해보고 싶어요",
        "특별한 날이나 응원을 준비하고 싶어요"
    )
}