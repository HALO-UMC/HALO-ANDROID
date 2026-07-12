package com.umc.halo.domain.model.storybook

/**
 * 스토리북 목록 화면에서 사용하는 도메인 모델
 * 사용자 맞춤 스토리북 추천 카드
 * 온보딩에서 고른 관계 방향 태그를 기반으로 추천되며 전체 탭에서만 2개 노출
 */
data class CustomStorybook(
    val id: Int,
    val tag: String,       // 추천 이유 태그 (예: "대화가 어색한 당신을 위한")
    val title: String,     // 스토리북 제목 (예: "오래전 당신")
    val subtitle: String   // 부제 (예: "가족과의 만남")
)

/**
 * 테마 섹션 안에 들어가는 스토리북
 */
data class Storybook(
    val id: Int,
    val title: String,
    val subtitle: String
    // TODO: 서버 연동 후 커버 이미지 추가 예정
)

/**
 * "대화가 어색할 때" 처럼 상황별로 스토리북을 묶은 테마 섹션
 */
data class StorybookTheme(
    val id: Int,
    val title: String,               // 섹션 제목 (예: "대화가 어색할 때")
    val storybooks: List<Storybook>  // 해당 상황에 추천되는 책들
)

/**
 * '진행중' 탭에 표시되는 스토리북
 * 목록은 시작일이 빠른 순으로 좌상단부터 배치 (정렬은 데이터 계층에서)
 */
data class InProgressStorybook(
    val id: Int,
    val title: String,
    val subtitle: String,
    val currentChapter: Int,  // "N장 진행중" 책갈피에 표시
    val isWaiting: Boolean     // 오늘 분량 완료
)
