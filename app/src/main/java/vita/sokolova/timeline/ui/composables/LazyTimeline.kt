package vita.sokolova.timeline.ui.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import vita.sokolova.timeline.R
import vita.sokolova.timeline.domain.entities.HiringStage
import vita.sokolova.timeline.domain.entities.HiringStageStatus
import vita.sokolova.timeline.ui.theme.Gray200
import vita.sokolova.timeline.ui.theme.Green500
import vita.sokolova.timeline.ui.theme.Orange500
import vita.sokolova.timeline.ui.composables.timeline.CircleParameters
import vita.sokolova.timeline.ui.composables.timeline.LineParameters
import vita.sokolova.timeline.ui.composables.timeline.StrokeParameters
import vita.sokolova.timeline.ui.composables.timeline.TimelineNode
import vita.sokolova.timeline.ui.composables.timeline.TimelineNodePosition

@Composable
fun LazyTimeline(stages: Array<HiringStage>) {
    LazyColumn(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        content = {
            itemsIndexed(stages) { index, hiringStage ->
                val position: TimelineNodePosition = mapToTimelineNodePosition(index, stages.size)
                TimelineNode(
                    position = position,
                    circleParameters = CircleParameters(
                        radius = 12.dp,
                        backgroundColor = getIconColor(hiringStage),
                        stroke = getIconStrokeColor(hiringStage),
                        icon = getIcon(hiringStage)
                    ),
                    lineParameters = getLineBrush(
                        with(LocalDensity.current) { 12.dp.toPx() }, index, stages
                    )?.let {
                        LineParameters(
                            strokeWidth = 3.dp,
                            brush = it
                        )
                    },
                    contentStartOffset = 16.dp,
                    spacer = 24.dp
                ) { modifier ->
                    Message(hiringStage, modifier)
                }
            }
        },
        contentPadding = PaddingValues(16.dp)
    )
}

@Composable
private fun getLineBrush(circleRadiusInPx: Float, index: Int, items: Array<HiringStage>): Brush? {
    return if (index != items.lastIndex) {
        val currentStage: HiringStage = items[index]
        val nextStage: HiringStage = items[index + 1]
        Brush.verticalGradient(
            colors = listOf(
                (getIconStrokeColor(currentStage)?.color ?: getIconColor(currentStage)),
                (getIconStrokeColor(nextStage)?.color ?: getIconColor(items[index + 1]))
            ),
            startY = circleRadiusInPx * 2
        )
    } else {
        null
    }

}

private fun getIconColor(stage: HiringStage): Color {
    return when (stage.status) {
        HiringStageStatus.FINISHED -> Green500
        HiringStageStatus.CURRENT -> Orange500
        HiringStageStatus.UPCOMING -> Color.White
    }
}

private fun getIconStrokeColor(stage: HiringStage): StrokeParameters? {
    return if (stage.status == HiringStageStatus.UPCOMING) {
        StrokeParameters(color = Gray200, width = 2.dp)
    } else {
        null
    }
}

@Composable
private fun getIcon(stage: HiringStage): Int? {
    return if (stage.status == HiringStageStatus.CURRENT) {
        R.drawable.ic_bubble_warning_16
    } else {
        null
    }
}

private fun mapToTimelineNodePosition(index: Int, collectionSize: Int) = when (index) {
    0 -> TimelineNodePosition.FIRST
    collectionSize - 1 -> TimelineNodePosition.LAST
    else -> TimelineNodePosition.MIDDLE
}