package com.umc.halo.domain.model.storybook

/**
 * 스토리북 목록 화면, 홈 화면, 테마보관함 화면에서 사용하는 도메인 모델
 * 사용자 맞춤 스토리북 추천 카드
 * 온보딩에서 고른 관계 방향 태그를 기반으로 추천되며 전체 탭에서만 2개 노출
 */
data class CustomStorybook(
    val id: Long,
    val tag: String,       // 추천 이유 태그 (예: "대화가 어색한 당신을 위한")
    val title: String,     // 스토리북 제목 (예: "오래전 당신")
    val subtitle: String,   // 부제 (예: "가족과의 만남")
    val imageUrl: String   // 이미지 url
)

/**
 * 사용자의 스토리북 진행 상태
 * 스토리북 목록의 전체 탭 -> 카드 배지(책갈피)에 사용 (없으면 아직 시작 전 → 배지 없음)
 */
sealed interface StorybookProgress {
    // 진행중: 현재 진행 장수
    data class InProgress(val chapter: Int) : StorybookProgress

    // 완료: 10장까지 모두 끝냄
    data object Done : StorybookProgress
}

/**
 * 테마 섹션 안에 들어가는 스토리북
 */
data class Storybook(
    val id: Long,
    val title: String,
    val subtitle: String,
    val imageUrl: String? = null,            // 커버 이미지 (null 이면 회색 플레이스홀더)
    val progress: StorybookProgress? = null  // 전체 탭 카드 배지용 (null = 배지 없음)
)

/**
 * "대화가 어색할 때" 처럼 상황별로 스토리북을 묶은 테마 섹션
 */
data class StorybookTheme(
    val title: String,               // 섹션 제목 = 상황 태그
    val storybooks: List<Storybook>  // 해당 상황에 추천되는 책들
)

/**
 * '진행중' 탭에 표시되는 스토리북
 */
data class InProgressStorybook(
    val id: Long,
    val title: String,
    val subtitle: String,
    val imageUrl: String? = null,
    val currentChapter: Int,  // "N장 진행중" 책갈피에 표시
    val isWaiting: Boolean     // 오늘 분량 완료 → 카드를 어둡게 하고 클릭 비활성화
)

/**
 * 스토리북 목록 화면이 API로 받아오는 데이터
 * 탭별 목록을 데이터 계층에서 분리함
 */
data class StorybookListResult(
    val themes: List<StorybookTheme>,                  // 전체 탭 - 상황별 테마 섹션들
    val inProgressStorybooks: List<InProgressStorybook>,  // 진행중 탭
    val doneStorybooks: List<Storybook>                // 완료 탭
)

