package com.umc.halo.presentation.terms

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.umc.halo.R
import com.umc.halo.domain.model.terms.TermsAgreement
import com.umc.halo.presentation.component.HaloTopBar
import com.umc.halo.presentation.onboarding.component.OnboardingBottomButton
import com.umc.halo.presentation.theme.Gray300
import com.umc.halo.presentation.theme.Gray30
import com.umc.halo.presentation.theme.Gray400
import com.umc.halo.presentation.theme.Gray500
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloTheme
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary30
import com.umc.halo.presentation.theme.Primary600
import com.umc.halo.presentation.theme.White

/**
 * 약관동의 화면 진입점
 *
 * - [onBack] : 상단바 뒤로가기 (약관동의 메인 → 이전 화면인 로그인 화면)
 * - [onNavigateToOnboarding] : '다음' 버튼 (약관 동의 완료 → 온보딩 화면)
 */
@Composable
fun TermsRoute(
    modifier: Modifier = Modifier,
    viewModel: TermsViewModel = viewModel(),
    onBack: () -> Unit = {},
    onNavigateToOnboarding: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    TermsScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onBack = onBack,
        onNavigateToOnboarding = onNavigateToOnboarding,
        modifier = modifier
    )
}

/**
 * 약관동의 화면
 * selectedTerm 이 있으면 상세 화면 없으면 약관목록 화면을 보여줌
 */
@Composable
fun TermsScreen(
    uiState: TermsUiState,
    onEvent: (TermsUiEvent) -> Unit,
    onBack: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedTerm = uiState.selectedTerm

    // 상세 화면일 때 시스템 뒤로가기 → 화면 전체를 벗어나지 않고 목록으로만 복귀
    BackHandler(enabled = selectedTerm != null) {
        onEvent(TermsUiEvent.DetailDismissed)
    }

    if (selectedTerm == null) {
        TermsAgreementContent(
            uiState = uiState,
            onEvent = onEvent,
            onBack = onBack,
            onNext = onNavigateToOnboarding,
            modifier = modifier
        )
    } else {
        TermsDetailContent(
            term = selectedTerm,
            onBack = { onEvent(TermsUiEvent.DetailDismissed) },
            onAgree = { onEvent(TermsUiEvent.DetailAgreeClicked(selectedTerm.id)) },
            modifier = modifier
        )
    }
}

/** 약관동의 목록 화면 */
@Composable
private fun TermsAgreementContent(
    uiState: TermsUiState,
    onEvent: (TermsUiEvent) -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
    ) {
        HaloTopBar(
            title = "약관동의",
            showLeftIcon = true,
            onClick = onBack
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(Modifier.height(24.dp))

            // 전체 동의
            TermsRow(
                text = "전체 동의",
                checked = uiState.isAllAgreed,
                onToggle = { onEvent(TermsUiEvent.AllAgreeToggled) },
                uncheckedTextColor = Gray500
            )

            Spacer(Modifier.height(36.dp))

            Text(
                text = "세부 약관",
                style = HaloType.body01SemiBold,
                color = Gray800,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(18.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                uiState.terms.forEach { term ->
                    TermsRow(
                        text = term.title,
                        checked = uiState.isAgreed(term.id),
                        onToggle = { onEvent(TermsUiEvent.TermToggled(term.id)) },
                        onDetailClick = { onEvent(TermsUiEvent.TermDetailClicked(term.id)) }
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
        }

        // 약관 미체크 시 '다음' 버튼 비활성
        OnboardingBottomButton(
            text = "다음",
            enabled = uiState.isNextEnabled,
            onClick = onNext,
            modifier = Modifier
                .navigationBarsPadding()
                .padding(start = 24.dp, end = 24.dp, bottom = 20.dp)
        )
    }
}

/** 약관 상세 화면 (전문 열람 후 '동의') */
@Composable
private fun TermsDetailContent(
    term: TermsAgreement,
    onBack: () -> Unit,
    onAgree: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
    ) {
        HaloTopBar(
            title = "약관동의",
            showLeftIcon = true,
            onClick = onBack
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(Modifier.height(24.dp))

            Text(
                text = "최종 업데이트 | ${term.lastUpdated}",
                style = HaloType.body03Regular,
                color = Gray500
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = term.detailHeading,
                style = HaloType.body01SemiBold,
                color = Gray800
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = term.detailContent,
                style = HaloType.body01Medium.copy(
                    lineHeight = 23.2.sp,
                    letterSpacing = (-0.16).sp
                ),
                color = Gray800
            )

            Spacer(Modifier.height(24.dp))
        }

        OnboardingBottomButton(
            text = "동의",
            enabled = true,
            onClick = onAgree,
            modifier = Modifier
                .navigationBarsPadding()
                .padding(start = 24.dp, end = 24.dp, bottom = 20.dp)
        )
    }
}

/**
 * 약관 한 줄
 *
 * 체크+라벨 영역을 누르면 동의 토글, > 영역을 누르면 상세로 이동
 *
 * @param onDetailClick null 이면 > 를 표시하지 않음
 * @param uncheckedTextColor 미체크 상태의 라벨 색
 */
@Composable
private fun TermsRow(
    text: String,
    checked: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    uncheckedTextColor: Color = Gray400,
    onDetailClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (checked) Primary30 else Gray30),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 체크 + 라벨: 누르면 동의 토글
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clickable(onClick = onToggle)
                .padding(start = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_terms_check),
                contentDescription = if (checked) "동의함" else "동의 안 함",
                tint = if (checked) Primary600 else Gray300,
                modifier = Modifier.size(width = 16.dp, height = 15.dp)
            )

            Spacer(Modifier.width(16.dp))

            Text(
                text = text,
                style = HaloType.body02SemiBold,
                color = if (checked) Primary600 else uncheckedTextColor,
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )
        }

        if (onDetailClick != null) {
            // >: 누르면 약관 상세로 이동
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .clickable(onClick = onDetailClick)
                    .padding(start = 8.dp, end = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_home_right_arrow),
                    contentDescription = "약관 상세 보기",
                    tint = Gray400,
                    modifier = Modifier.size(24.dp)
                )
            }
        } else {
            // > 가 없는 행
            Spacer(Modifier.width(24.dp))
        }
    }
}

// ---- Preview ----

private val previewTerms = listOf(
    TermsAgreement("t1", "개인정보 동의"),
    TermsAgreement("t2", "개인정보 동의"),
    TermsAgreement("t3", "개인정보 동의"),
    TermsAgreement("t4", "개인정보 동의")
)

@Preview(showBackground = true, showSystemUi = true, name = "약관동의 - 미동의")
@Composable
private fun TermsScreenEmptyPreview() {
    HaloTheme {
        TermsScreen(
            uiState = TermsUiState(terms = previewTerms),
            onEvent = {},
            onBack = {},
            onNavigateToOnboarding = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "약관동의 - 전체동의")
@Composable
private fun TermsScreenAgreedPreview() {
    HaloTheme {
        TermsScreen(
            uiState = TermsUiState(
                terms = previewTerms,
                agreedIds = previewTerms.map { it.id }.toSet()
            ),
            onEvent = {},
            onBack = {},
            onNavigateToOnboarding = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "약관 상세")
@Composable
private fun TermsDetailPreview() {
    HaloTheme {
        TermsScreen(
            uiState = TermsUiState(
                terms = previewTerms.map {
                    it.copy(
                        lastUpdated = "26.06.13",
                        detailHeading = "개인정보 약관",
                        detailContent = "내용을 적어주세요~".repeat(30)
                    )
                },
                selectedTermId = "t1"
            ),
            onEvent = {},
            onBack = {},
            onNavigateToOnboarding = {}
        )
    }
}
