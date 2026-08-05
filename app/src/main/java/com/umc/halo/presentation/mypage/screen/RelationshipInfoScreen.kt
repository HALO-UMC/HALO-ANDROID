package com.umc.halo.presentation.mypage.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umc.halo.presentation.mypage.component.MyPageContainer
import com.umc.halo.presentation.mypage.component.MyPageTopBar
import com.umc.halo.presentation.mypage.relationship.RelationshipDisplayItem
import com.umc.halo.presentation.mypage.relationship.RelationshipInfoUiState
import com.umc.halo.presentation.theme.Gray100
import com.umc.halo.presentation.theme.Gray800
import com.umc.halo.presentation.theme.HaloType
import com.umc.halo.presentation.theme.Primary400
import com.umc.halo.presentation.theme.Primary50
import com.umc.halo.presentation.theme.Primary500
import com.umc.halo.presentation.theme.Primary600

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RelationshipInfoScreen(
    uiState: RelationshipInfoUiState,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    MyPageContainer(modifier = modifier) {
        MyPageTopBar(title = "관계 정보", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 35.dp, bottom = 32.dp)
        ) {
            Text(
                text = "부모님의 성격/성향",
                style = HaloType.body01Medium,
                color = Gray800
            )
            Spacer(modifier = Modifier.height(12.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                maxItemsInEachRow = 3
            ) {
                uiState.parentPersonalityDisplayTags.forEach { tag ->
                    ParentPersonalityChip(tag)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = Gray100)
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "부모님과 나의 관계는?",
                style = HaloType.body01Medium,
                color = Gray800
            )
            Spacer(modifier = Modifier.height(12.dp))
            RelationshipAnswerCard(
                item = uiState.currentRelationStateDisplayItem,
                showDescription = true
            )

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = Gray100)
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "어떤 사이가 되고 싶나요?",
                style = HaloType.body01Medium,
                color = Gray800
            )
            Spacer(modifier = Modifier.height(12.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                uiState.goalRelationshipDisplayItems.forEach { item ->
                    RelationshipAnswerCard(
                        item = item,
                        showDescription = false
                    )
                }
            }
        }
    }
}

@Composable
private fun ParentPersonalityChip(text: String) {
    Surface(
        shape = RoundedCornerShape(100.dp),
        color = Primary50
    ) {
        Text(
            text = text,
            style = HaloType.body02Medium.copy(
                fontSize = 12.sp,
                lineHeight = 16.sp
            ),
            color = Primary500,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun RelationshipAnswerCard(
    item: RelationshipDisplayItem,
    showDescription: Boolean
) {
    val visibleDescription = item.description
        ?.takeIf { showDescription }
        ?.takeIf { it.isNotBlank() }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Primary50)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = item.title,
                style = HaloType.body02Medium.copy(
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                ),
                color = Primary600
            )
            if (visibleDescription != null) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = visibleDescription,
                    style = HaloType.caption01Regular.copy(
                        fontSize = 10.sp,
                        lineHeight = 14.sp
                    ),
                    color = Primary400
                )
            }
        }
    }
}
