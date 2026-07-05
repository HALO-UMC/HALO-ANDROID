package com.umc.halo.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// 앱 색상 세트
private val HaloColorScheme = lightColorScheme(
    primary          = Primary500,   // 앱 대표색
    onPrimary        = White,        // primary 색 위 글자색

    background        = White,       // 화면 기본 배경
    onBackground      = Black,       // 배경 위 기본 글자색

    surface          = White,        // 카드·시트 등의 표면색
    onSurface        = Black,        // surface 위 글자색

    error            = Error,        // 에러 상태 색
    onError          = White         // error 색 위 글자색
)

// 앱 기본 테마
@Composable
fun HaloTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = HaloColorScheme,
        content = content
        // 폰트는 typography 형식을 따로 만들어야 하기에 일단 생략
    )
}