package com.umc.halo.domain.repository.onboarding

/**
 * 온보딩 저장소 인터페이스 (도메인 계층)
 *
 * TODO(온보딩 담당): 온보딩 입력값 저장(POST /api/v1/onboarding)과
 *  '이어하기'용 currentStep·savedData 조회는 여기에 추가
 *  지금은 로그인 흐름 분기에 필요한 완료 여부만 구현
 */
interface OnboardingRepository {

    /**
     * 온보딩을 완료했는지 여부
     * 앱 실행(스플래시)에서 온보딩 화면으로 보낼지 홈으로 보낼지 판단하는 기준
     *
     * @return 조회 실패 시 false
     */
    suspend fun isOnboardingCompleted(): Boolean
}
