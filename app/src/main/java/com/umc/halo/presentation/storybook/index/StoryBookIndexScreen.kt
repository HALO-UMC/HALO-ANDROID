package com.umc.halo.presentation.storybook.index

import android.R.attr.onClick
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.umc.halo.R
import com.umc.halo.presentation.component.ButtonState
import com.umc.halo.presentation.component.HaloMaterialButton
import com.umc.halo.presentation.home.HomeScreen
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Gray200
import com.umc.halo.presentation.theme.Gray30
import com.umc.halo.presentation.theme.Gray300
import com.umc.halo.presentation.theme.Gray400
import com.umc.halo.presentation.theme.Gray50
import com.umc.halo.presentation.theme.Gray500
import com.umc.halo.presentation.theme.Gray600
import com.umc.halo.presentation.theme.Gray700
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary30
import com.umc.halo.presentation.theme.Primary500
import com.umc.halo.presentation.theme.White
import kotlin.io.path.Path
import kotlin.io.path.moveTo


private val ScreenPaddingHorizontal = 24.dp
private val CoverPlaceholderColor = Gray100 // TODO: 실제 커버 이미지로 추후 교체


data class StoryBookIndex(
    val id: Int,
    val title: String,
    val subTitle: String,
    val isLocked: Boolean
)
@Composable
fun StoryBookIndexScreen(
    indexList: List<StoryBookIndex>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = ScreenPaddingHorizontal)
    ) {
        item {
            StoryBookIndexIntro()

            Spacer(Modifier.height(56.dp))

            Text(
                text = "이야기의 차례",
                style = HaloType.body01SemiBold,
                color = Gray800
            )

            Spacer(Modifier.height(18.dp))
        }

        items(
            items = indexList,
            key = { item -> item.id },
        ) { item ->
            StoryBookIndex(item)
        }
    }
}

@Composable
fun StoryBookIndexIntro() {
    Column() {
        Spacer(Modifier.height(20.dp))

        StoryThemeIntro()

        Spacer(Modifier.height(36.dp))

        StoryBookProgress()

        Spacer(Modifier.height(48.dp))

        TodayStoryBook()
    }
}

private val StoryThemeIntroPadding = 12.dp
@Composable
fun StoryThemeIntro() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "스토리 테마 소개",
            style = HaloType.body01SemiBold,
            color = Gray800,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(StoryThemeIntroPadding))

        Box(
            modifier = Modifier
                .height(132.dp)
                .width(120.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(CoverPlaceholderColor)
                .align(Alignment.CenterHorizontally)
        ) {
            //이미지 추가
        }

        Spacer(Modifier.height(StoryThemeIntroPadding))

        Text(
            text = "내용",
            style = HaloType.body03Regular,
            color = Gray500,
            modifier = Modifier.fillMaxWidth()
        )
    }
}






@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    StoryBookIndexScreen(dummyData)
}


val dummyData = listOf(
    StoryBookIndex(1,"나와 같은 나이였던 시절","부모님이 지금의 내 나이였을 때의 하루",false),
    StoryBookIndex(2,"나와 같은 나이였던 시절","부모님이 지금의 내 나이였을 때의 하루",false),
    StoryBookIndex(3,"나와 같은 나이였던 시절","부모님이 지금의 내 나이였을 때의 하루",false)
)