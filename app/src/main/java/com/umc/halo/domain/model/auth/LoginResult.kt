package com.umc.halo.domain.model.auth

/**
 * 로그인 성공 결과
 *
 * @param isNewUser           이번 로그인으로 새로 가입된 회원인지
 * @param onboardingCompleted 온보딩 완료 여부
 * @param termsAgreed         필수 약관 전체 동의 여부. 서버가 값을 주지 않으면 null
 *
 *   isNewUser 로 '약관 동의 여부'를 판단하지 않음
 *   서버는 로그인 시점에 이미 회원을 생성하므로(소셜로그인 API 명세 참고),
 *   약관 화면에서 이탈한 사용자도 다음 로그인 때는 isNewUser=false 로 내려오기에
 *   그 값으로 분기하면 약관을 건너뛰고 온보딩으로 점프함
 *   약관 여부는 서버가 내려주는 termsAgreed 로만 판단
 */
data class LoginResult(
    val isNewUser: Boolean,
    val onboardingCompleted: Boolean,
    val termsAgreed: Boolean?
)
