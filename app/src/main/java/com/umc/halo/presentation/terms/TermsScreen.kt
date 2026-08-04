package com.umc.halo.presentation.terms

import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.umc.halo.R
import com.umc.halo.domain.model.auth.AuthDestination
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
 * 이동은 두 방향
 * - [onNavigateToOnboarding] : '다음'(동의 내역 저장 성공) → 온보딩
 * - [onNavigateToLogin] : 상단바 뒤로가기 → 로그아웃 후 로그인
 *
 *
 * 자동 로그인(스플래시)으로 약관에 바로 들어온 경우에는 백스택에 로그인 화면이 없으므로
 * 뒤로가기를 popBackStack 이 아니라 명시적 이동으로 처리
 */
@Composable
fun TermsRoute(
    modifier: Modifier = Modifier,
    viewModel: TermsViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit = {},
    onNavigateToOnboarding: () -> Unit = {}
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.destination) {
        when (uiState.destination ?: return@LaunchedEffect) {
            AuthDestination.ONBOARDING -> onNavigateToOnboarding()
            AuthDestination.LOGIN -> onNavigateToLogin()
            // 약관 화면에서 이 두 곳으로 갈 일은 없음
            AuthDestination.TERMS, AuthDestination.HOME -> Unit
        }
        viewModel.onEvent(TermsUiEvent.NavigationHandled)
    }

    LaunchedEffect(uiState.errorMessage) {
        val message = uiState.errorMessage ?: return@LaunchedEffect
        // TODO: 에러 표시 방식은 디자인 확정 후 교체
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        viewModel.onEvent(TermsUiEvent.ErrorShown)
    }

    TermsScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
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
            onBack = { onEvent(TermsUiEvent.BackClicked) },
            onNext = { onEvent(TermsUiEvent.NextClicked) },
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
                textStyle = HaloType.body02SemiBold,
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

            // 필수/선택은 "(필수) "/"(선택) " 으로 표기
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                uiState.terms.forEach { term ->
                    TermsRow(
                        text = term.displayTitle,
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
                style = HaloType.body01Medium,
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
 * 필수/선택 표기가 붙은 약관 제목
 */
private val TermsAgreement.displayTitle: String
    get() = if (required) "(필수) $title" else "(선택) $title"

/**
 * 약관 한 줄
 *
 * 체크+라벨 영역을 누르면 동의 토글, > 영역을 누르면 상세로 이동
 *
 * @param onDetailClick null 이면 > 를 표시하지 않음
 * @param textStyle 라벨 글자 스타일. 전체 동의만 SemiBold 이고 세부 약관은 Medium
 * @param uncheckedTextColor 미체크 상태의 라벨 색
 */
@Composable
private fun TermsRow(
    text: String,
    checked: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = HaloType.body02Medium,
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
                style = textStyle,
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

// 서버 응답과 같은 구성 (termId 1~3 필수 / 4 선택)
private val previewTerms = listOf(
    TermsAgreement(1L, "서비스 이용약관 동의"),
    TermsAgreement(2L, "개인정보 처리방침 동의"),
    TermsAgreement(3L, "콘텐츠 보관 및 활용 안내 확인"),
    TermsAgreement(4L, "마케팅 정보 수신 동의", required = false)
)

@Preview(showBackground = true, showSystemUi = true, name = "약관동의 - 미동의")
@Composable
private fun TermsScreenEmptyPreview() {
    HaloTheme {
        TermsScreen(
            uiState = TermsUiState(terms = previewTerms),
            onEvent = {}
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
            onEvent = {}
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
                selectedTermId = 1L
            ),
            onEvent = {}
        )
    }
}
