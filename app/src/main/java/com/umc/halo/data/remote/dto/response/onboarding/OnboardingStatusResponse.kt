package com.umc.halo.data.remote.dto.response.onboarding

/**
 * GET /api/v1/onboarding/status 응답
 *
 * @param onboardingCompleted 온보딩 완료 여부
 * @param currentStep 이어할 단계 (1~5, 시작 전이면 null)
 *
 * TODO(온보딩 담당): 응답에는 savedData(name·gender·birthDate·태그 ID들)도 함께 오는데,
 *  '이어하기'(마지막 단계부터 재개 + 입력값 복원) 기능이 온보딩 화면 소관이라 여기서는 빼둠
 *  Gson 은 모르는 필드를 무시하므로 이 DTO 에 savedData 를 추가하기만 하면 바로 사용 가능
 *  로그인 흐름 분기에서는 onboardingCompleted 만 사용하도록 구현함
 */
data class OnboardingStatusResponse(
    val onboardingCompleted: Boolean,
    val currentStep: Int?
)
