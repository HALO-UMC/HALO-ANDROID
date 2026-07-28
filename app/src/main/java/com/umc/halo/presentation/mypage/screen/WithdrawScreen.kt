package com.umc.halo.presentation.mypage.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.halo.R
import com.umc.halo.presentation.mypage.MyPageUiEvent
import com.umc.halo.presentation.mypage.MyPageUiState
import com.umc.halo.presentation.mypage.component.ConfirmActionDialog
import com.umc.halo.presentation.mypage.component.MyPageContainer
import com.umc.halo.presentation.mypage.component.MyPageTopBar
import com.umc.halo.presentation.mypage.component.PrimaryActionButton
import com.umc.halo.presentation.mypage.component.SecondaryActionButton
import com.umc.halo.presentation.mypage.component.WarningLine
import com.umc.halo.presentation.theme.Gray600
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType

/**
 * TODO(마이페이지 담당): onNavigateToLogin 파라미터를 제거함
 *  탈퇴는 서버 계정 삭제 + 소셜 연결 해제가 필요해서 '탈퇴하기'가 WithdrawConfirmed 이벤트를 보내고
 *  처리가 끝나면 WithdrawRoute 가 navigateToLogin 신호를 보고 이동시킴
 *  (소셜 연결 해제에 Activity Context 가 필요해 이벤트에 담아 보냄)
 */
@Composable
fun WithdrawScreen(
    uiState: MyPageUiState,
    onEvent: (MyPageUiEvent) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    if (uiState.showWithdrawDialog) {
        ConfirmActionDialog(
            title = "정말 탈퇴하시겠어요?",
            description = "탈퇴 시 모든 정보가 사라집니다.",
            buttonText = "탈퇴하기",
            onDismiss = {
                onEvent(MyPageUiEvent.WithdrawDialogChanged(false))
            },
            onConfirm = { onEvent(MyPageUiEvent.WithdrawConfirmed(context)) }
        )
    }

    MyPageContainer(modifier = modifier) {
        MyPageTopBar(title = "회원 탈퇴", onBack = onBack)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(top = 58.dp, bottom = 142.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "정말 탈퇴하시겠어요?",
                    style = HaloType.heading02SemiBold.copy(
                        fontSize = 24.sp,
                        lineHeight = 36.sp
                    ),
                    color = Gray800,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "회원 탈퇴 시 계정 정보와 진행 중인 스토리북, 저장된 모든 기록이 삭제되며, 삭제된 데이터는 복구할 수 없어요.",
                    style = HaloType.body02Regular.copy(
                        fontSize = 14.sp,
                        lineHeight = 20.3.sp
                    ),
                    color = Gray600,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(54.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_orange_character),
                    contentDescription = null,
                    modifier = Modifier.size(width = 94.dp, height = 109.dp)
                )

                Spacer(Modifier.height(42.dp))
                WarningLine("계정 정보가 삭제돼요.")
                WarningLine("기록과 스토리북이 모두 사라져요")
                WarningLine("삭제 후에는 복구할 수 없어요.")
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp)
            ) {
                PrimaryActionButton(
                    text = "탈퇴 할게요",
                    onClick = {
                        onEvent(MyPageUiEvent.WithdrawDialogChanged(true))
                    }
                )
                Spacer(Modifier.height(12.dp))
                SecondaryActionButton(text = "취소", onClick = onBack)
            }
        }
    }
}
