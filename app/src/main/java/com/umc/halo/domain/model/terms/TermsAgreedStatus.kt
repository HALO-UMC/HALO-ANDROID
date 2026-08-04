package com.umc.halo.domain.model.terms

/**
 * 사용자의 약관 동의 현황 (GET /api/v1/terms/agreements)
 *
 * @param allRequiredAgreed 필수 약관을 전부 동의했는지.
 *   로그인/스플래시에서 약관 화면을 띄울지 판단하는 기준.
 * @param agreedTermIds 동의한 약관 id 집합. 약관 화면에 되돌아왔을 때 체크 상태를 되살리는 데 사용
 *
 * [agreedTermIds] 의 null 과 빈 집합은 뜻이 다름
 * - `null`         = 서버가 약관별 내역을 주지 않음 (아직 미구현) → 알 수 없으니 다른 방법으로 추정해야 함
 * - `emptySet()`   = 서버가 알려줬고, 동의한 약관이 하나도 없음
 * 둘을 같게 다루면 이미 동의한 사용자의 체크가 풀린 채 저장될 수 있음
 */
data class TermsAgreedStatus(
    val allRequiredAgreed: Boolean,
    val agreedTermIds: Set<Long>? = null
)
