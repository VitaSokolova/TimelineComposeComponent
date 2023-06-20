package vita.sokolova.timeline

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import vita.sokolova.timeline.ui.theme.Green500
import vita.sokolova.timeline.ui.theme.Green700
import vita.sokolova.timeline.ui.theme.Orange500
import vita.sokolova.timeline.ui.theme.TimelineComposeComponentTheme
import vita.sokolova.timeline.ui.timeline.CircleParameters
import vita.sokolova.timeline.ui.timeline.LineParameters
import vita.sokolova.timeline.ui.timeline.StrokeParameters
import vita.sokolova.timeline.ui.timeline.TimelineNode
import vita.sokolova.timeline.ui.timeline.TimelineNodePosition

class SampleActivity : ComponentActivity() {

    //Replace with more interesting example: like cooking recipe
    private val timelineStages = arrayOf(
        Stage(
            "Book a Alice's Adventures in Wonderland (commonly Alice in Wonderland) is an 1865 English children's novel by Lewis Carroll, a mathematics don at Oxford University. It details the story of a young girl named Alice who falls through a rabbit hole into a fantasy world of anthropomorphic creatures.",
            true
        ),
        Stage(
            "It is seen as an example of the literary nonsense genre. The artist John Tenniel provided 42 wood-engraved illustrations for the book.",
            true
        ),
        Stage(
            "It received positive reviews upon release and is now one of the best-known works of Victorian literature; ",
            false
        ),
        Stage(
            "its narrative, structure, characters and imagery have had widespread influence on popular culture and literature, especially in the fantasy genre.",
            false
        ),
        Stage(
            "It is credited as helping end an era of didacticism in children's literature", true
        ),
        Stage(
            "Inaugurating an era in which writing for children aimed to \"delight or entertain\"",
            true
        ),
        Stage(
            "The tale plays with logic, giving the story lasting popularity with adults as well as with children.",
            false
        ),
        Stage(
            "The titular character Alice shares her name with Alice Liddell, a girl Carroll knew.",
            false
        ),
        Stage(
            "The book has never been out of print and has been translated into 174 languages.", true
        ),
        Stage(
            "Its legacy covers adaptations for screen, radio, art, ballet, opera, musicals, theme parks, board games and video games.",
            true
        ),
        Stage(
            "Carroll published a sequel in 1871 entitled Through the Looking-Glass and a shortened version for young children, The Nursery \"Alice\" in 1890.",
            false
        ),
        Stage("\"All in the golden afternoon...\"", false)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TimelineComposeComponentTheme {
                SampleScreenContent(timelineStages)
            }
        }
    }
}

data class Stage(
    val description: String, val finished: Boolean
)

@Composable
fun SampleScreenContent(timelineStages: Array<Stage>) {
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyTimeline(timelineStages)
    }
}

@Composable
fun LazyTimeline(stages: Array<Stage>) {
    LazyColumn(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(), content = {
            itemsIndexed(stages) { index, message ->
                val position: TimelineNodePosition = mapToTimelineNodePosition(index, stages.size)
                TimelineNode(
                    position = position, circleParameters = CircleParameters(
                        radius = 12.dp,
                        backgroundColor = getIconColor(message),
                        stroke = getIconStrokeColor(message),
                        icon = getIcon(message)
                    ), lineParameters = LineParameters(
                        strokeWidth = 3.dp, getLineBrush(index, stages)
                    ), contentStartOffset = 16.dp, spacer = 32.dp
                ) { modifier ->
                    Text(modifier = modifier, text = message.description)
                }
            }
        },
        contentPadding = PaddingValues(16.dp)
    )
}

private fun getLineBrush(index: Int, items: Array<Stage>): Brush {
    val message: Stage = items[index]
    return Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to getIconColor(message), 1f to if (index != items.lastIndex) {
                getIconColor(items[index + 1])
            } else {
                getIconColor(message)
            }
        )
    )
}

private fun getIconColor(stage: Stage) = if (stage.finished) {
    Green500
} else {
    Orange500
}

private fun getIconStrokeColor(stage: Stage): StrokeParameters? {
    return if (stage.finished) {
        StrokeParameters(color = Green700, width = 2.dp)
    } else {
        null
    }
}

@Composable
private fun getIcon(stage: Stage): Int? {
    return if (stage.finished) {
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

    val testStageLists = arrayOf(
        Stage(
            "Book a hotel.", true
        ),
        Stage("Buy railway and plain tickets.", true),
        Stage("Find a medical insurance", false),
        Stage("Find someone to take care of our cat", false)
    )

    TimelineComposeComponentTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            LazyTimeline(testStageLists)
        }
    }
}


