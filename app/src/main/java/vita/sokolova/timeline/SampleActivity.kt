package vita.sokolova.timeline

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import vita.sokolova.timeline.SampleViewModel.Companion.TEST_DATA
import vita.sokolova.timeline.domain.HiringStage
import vita.sokolova.timeline.domain.HiringStageStatus
import vita.sokolova.timeline.domain.MessageSender
import vita.sokolova.timeline.ui.theme.Gray200
import vita.sokolova.timeline.ui.theme.Green500
import vita.sokolova.timeline.ui.theme.Orange500
import vita.sokolova.timeline.ui.theme.TimelineComposeComponentTheme
import vita.sokolova.timeline.ui.timeline.CircleParameters
import vita.sokolova.timeline.ui.timeline.LineParameters
import vita.sokolova.timeline.ui.timeline.StrokeParameters
import vita.sokolova.timeline.ui.timeline.TimelineNode
import vita.sokolova.timeline.ui.timeline.TimelineNodePosition
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import vita.sokolova.timeline.ui.theme.PurpleGrey80

class SampleActivity : ComponentActivity() {

    private val viewModel: SampleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TimelineComposeComponentTheme {
                val screenState = viewModel.hiringProcessState.collectAsState(initial = arrayOf())
                SampleScreenContent(screenState.value)
            }
        }
    }
}

@Composable
fun SampleScreenContent(timelineStages: Array<HiringStage>) {
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyTimeline(timelineStages)
    }
}

@Composable
fun LazyTimeline(stages: Array<HiringStage>) {
    LazyColumn(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(), content = {
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
                    contentStartOffset = 16.dp, spacer = 32.dp
                ) { modifier ->
                    createMessage(hiringStage, modifier)
                }
            }
        },
        contentPadding = PaddingValues(16.dp)
    )
}

@Composable
private fun BoxScope.createMessage(
    hiringStage: HiringStage,
    modifier: Modifier,
) {
    val textStyle = if (hiringStage.status == HiringStageStatus.CURRENT) {
        MaterialTheme.typography.bodyLarge
    } else {
        MaterialTheme.typography.bodyMedium
    }

    val textColor = if (hiringStage.status == HiringStageStatus.UPCOMING) {
        MaterialTheme.colorScheme.onSurfaceVariant.copy(
            alpha = 0.63f
        )
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    val (textAlign, boxAlign) = if (hiringStage.initiator is MessageSender.Candidate) {
        TextAlign.End to Alignment.CenterEnd
    } else {
        TextAlign.Start to Alignment.CenterStart
    }

    Box(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .width(220.dp)
                .align(boxAlign)
                .padding(8.dp),
            text = hiringStage.initiator.message,
            textAlign = textAlign,
            style = textStyle,
            color = textColor
        )
    }

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

@Preview(showBackground = true)
@Composable
fun TimelineUsagePreview() {

    TimelineComposeComponentTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            LazyTimeline(TEST_DATA)
        }
    }
}


