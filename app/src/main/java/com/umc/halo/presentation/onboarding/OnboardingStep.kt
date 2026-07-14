package com.umc.halo.presentation.onboarding

enum class OnboardingStep {
    NAME,
    BASIC_INFO,
    WELCOME,
    PARENT_PERSONALITY,
    RELATIONSHIP,
    GOAL,
    COMPLETE;

    fun next(): OnboardingStep {
        return when (this) {
            NAME -> BASIC_INFO
            BASIC_INFO -> WELCOME
            WELCOME -> PARENT_PERSONALITY
            PARENT_PERSONALITY -> RELATIONSHIP
            RELATIONSHIP -> GOAL
            GOAL -> COMPLETE
            COMPLETE -> COMPLETE
        }
    }

    fun previous(): OnboardingStep {
        return when (this) {
            NAME -> NAME
            BASIC_INFO -> NAME
            WELCOME -> BASIC_INFO
            PARENT_PERSONALITY -> WELCOME
            RELATIONSHIP -> PARENT_PERSONALITY
            GOAL -> RELATIONSHIP
            COMPLETE -> GOAL
        }
    }

    val progressIndex: Int?
        get() = when (this) {
            PARENT_PERSONALITY -> 0
            RELATIONSHIP -> 1
            GOAL -> 2
            else -> null
        }
}