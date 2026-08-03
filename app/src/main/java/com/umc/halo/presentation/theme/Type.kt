package com.umc.halo.presentation.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.umc.halo.R

// 폰트 파일 등록 (res/font의 4개 파일 연결)
val Pretendard = FontFamily(
    Font(R.font.pretendard_regular, FontWeight.Normal),     // Regular
    Font(R.font.pretendard_medium, FontWeight.Medium),     // Medium
    Font(R.font.pretendard_semibold, FontWeight.SemiBold), // SemiBold
    Font(R.font.pretendard_bold, FontWeight.Bold)          // Bold
)

// 디자인 시스템 글자 스타일 목록
object HaloType {

    // -- Title --
    val title01Bold = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = lineHeight(32, 150),
        letterSpacing = letterSpacing(32, -2)
    )

    val title01SemiBold = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = lineHeight(32, 150),
        letterSpacing = letterSpacing(32, -2)
    )

    val title02Bold = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = lineHeight(28, 150),
        letterSpacing = letterSpacing(28, -2)
    )

    val title02SemiBold = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = lineHeight(28, 150),
        letterSpacing = letterSpacing(28, -2)
    )

    // -- Heading --
    val heading01Bold = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = lineHeight(24, 150),
        letterSpacing = letterSpacing(24, -2)
    )

    val heading01SemiBold = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = lineHeight(24, 150),
        letterSpacing = letterSpacing(24, -2)
    )

    // 온보딩 제목처럼 Regular와 SemiBold가 섞이는 경우 사용하는 스타일
    val heading01Regular = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = lineHeight(24, 150),
        letterSpacing = letterSpacing(24, -2)
    )

    val heading02Bold = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = lineHeight(20, 150),
        letterSpacing = letterSpacing(20)
    )

    val heading02SemiBold = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = lineHeight(20, 150),
        letterSpacing = letterSpacing(20)
    )

    val heading02Medium = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = lineHeight(20, 150),
        letterSpacing = letterSpacing(20)
    )

    val heading02Regular = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = lineHeight(20, 150),
        letterSpacing = letterSpacing(20)
    )

    val heading03SemiBold = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = lineHeight(18, 150),
        letterSpacing = letterSpacing(18)
    )

    val heading03Medium = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = lineHeight(18, 150),
        letterSpacing = letterSpacing(18)
    )

    // -- Body --
    val body01SemiBold = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = lineHeight(16, 145),
        letterSpacing = letterSpacing(16)
    )

    val body01Medium = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = lineHeight(16, 145),
        letterSpacing = letterSpacing(16)
    )

    val body01Regular = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = lineHeight(16, 145),
        letterSpacing = letterSpacing(16)
    )

    val body02SemiBold = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = lineHeight(14, 145),
        letterSpacing = letterSpacing(14)
    )

    val body02Medium = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = lineHeight(14, 145),
        letterSpacing = letterSpacing(14)
    )

    val body02Regular = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = lineHeight(14, 145),
        letterSpacing = letterSpacing(14)
    )

    val body03Medium = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = lineHeight(12, 145),
        letterSpacing = letterSpacing(12)
    )

    val body03Regular = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = lineHeight(12, 145),
        letterSpacing = letterSpacing(12)
    )

    // -- Caption --
    val caption01Medium = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = lineHeight(10, 145),
        letterSpacing = letterSpacing(10)
    )

    val caption01Regular = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        lineHeight = lineHeight(10, 145),
        letterSpacing = letterSpacing(10)
    )

    val caption02Medium = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        fontSize = 8.sp,
        lineHeight = lineHeight(8, 145),
        letterSpacing = letterSpacing(8)
    )

    val caption02Regular = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 8.sp,
        lineHeight = lineHeight(8, 145),
        letterSpacing = letterSpacing(8)
    )
}


/**
 * 행간 수치 계산
 * @param fontSize 폰트 크기,
 * @param percent 행간 수치 퍼센테이지 (%)
 */
private fun lineHeight(fontSize: Int, percent: Int) =
    (fontSize * percent / 100f).sp

private fun letterSpacing(fontSize: Int, percent: Int = -1) =
    (fontSize * percent / 100f).sp
