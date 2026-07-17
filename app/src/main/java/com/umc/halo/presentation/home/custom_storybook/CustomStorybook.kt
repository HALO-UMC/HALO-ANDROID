package com.umc.halo.presentation.home.custom_storybook

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umc.halo.R
import com.umc.halo.domain.model.home.CustomizedStoryBooks
import com.umc.halo.domain.model.storybook.CustomStorybook
import com.umc.halo.presentation.component.CustomStorybookCard
import com.umc.halo.presentation.home.HomeUiEvent
import com.umc.halo.presentation.home.HomeViewModel
import com.umc.halo.presentation.theme.Black
import com.umc.halo.presentation.theme.Gray500
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary500


@Composable
fun CustomStorybook(
    customStorybookList: List<CustomStorybook>,
    onEvent: (HomeUiEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "맞춤 스토리북",
            style = HaloType.body02Medium,
            color = Color(0xFF3C3A35)
        )

        Spacer(Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = customStorybookList,
                key = { item -> item.id }
            ) { item ->
                CustomStorybookCard(item) {
                    onEvent(HomeUiEvent.OnCustomizedStoryBookClicked(item.id))
                } // 공용 component 이용
            }
        }
    }
}