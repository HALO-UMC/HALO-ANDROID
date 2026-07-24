package com.umc.halo.presentation.home.custom_storybook


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umc.halo.domain.model.storybook.CustomStorybook
import com.umc.halo.presentation.component.CustomStorybookCard
import com.umc.halo.presentation.home.HomeUiEvent
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary500


@Composable
fun CustomStorybook(
    items: List<CustomStorybook>,
    onEvent: (HomeUiEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = "맞춤 스토리북 추천",
            style = HaloType.body01SemiBold,
            color = Color(0xFF3C3A35)
        )

        Spacer(Modifier.height(12.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items.forEach { item ->
                CustomStorybookCard(
                    item = item,
                    onClick = { onEvent(HomeUiEvent.OnCustomizedStoryBookClicked(item.id)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}