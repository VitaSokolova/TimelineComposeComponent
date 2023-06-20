package vita.sokolova.timeline.ui.timeline

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import vita.sokolova.timeline.R
import vita.sokolova.timeline.ui.theme.Coral
import vita.sokolova.timeline.ui.theme.LightBlue
import vita.sokolova.timeline.ui.theme.LightCoral
import vita.sokolova.timeline.ui.theme.Purple
import vita.sokolova.timeline.ui.theme.TimelineComposeComponentTheme

@Composable
fun TimelineNode(
    position: TimelineNodePosition,
    circleParameters: CircleParameters,
    lineParameters: LineParameters,
    contentStartOffset: Dp,
    spacer: Dp,
    content: @Composable BoxScope.(modifier: Modifier) -> Unit
) {
    val iconPainter = circleParameters.icon?.let { painterResource(id = it) }
    Box(
        modifier = Modifier
            .wrapContentSize()
            .drawBehind {
                val circleRadiusInPx = circleParameters.radius.toPx()

                if (position != TimelineNodePosition.LAST) {
                    drawLine(
                        brush = lineParameters.brush,
                        start = Offset(circleRadiusInPx, circleRadiusInPx * 2),
                        end = Offset(circleRadiusInPx, this.size.height),
                        strokeWidth = lineParameters.strokeWidth.toPx()
                    )
                }

                drawCircle(
                    circleParameters.backgroundColor,
                    circleRadiusInPx,
                    center = Offset(circleRadiusInPx, circleRadiusInPx)
                )

                circleParameters.stroke?.let { stroke ->
                    val strokeWidthInPx = stroke.width.toPx()
                    drawCircle(
                        color = stroke.color,
                        radius = circleRadiusInPx - strokeWidthInPx / 2,
                        center = Offset(circleRadiusInPx, circleRadiusInPx),
                        style = Stroke(width = strokeWidthInPx)
                    )
                }

                iconPainter?.let { painter ->
                    this.withTransform({
                        translate(
                            left = circleRadiusInPx - painter.intrinsicSize.width / 2f,
                            top = circleRadiusInPx - painter.intrinsicSize.height / 2f
                        )
                    }, {
                        this.drawIntoCanvas {
                            with(painter) {
                                draw(intrinsicSize)
                            }
                        }
                    })
                }
            }
    ) {
        content(
            Modifier
                .defaultMinSize(minHeight = circleParameters.radius * 2)
                .padding(
                    start = circleParameters.radius * 2 + contentStartOffset,
                    bottom = if (position != TimelineNodePosition.LAST) spacer else 0.dp
                )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TimelinePreview() {
    TimelineComposeComponentTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TimelineNode(
                position = TimelineNodePosition.FIRST,
                circleParameters = CircleParametersDefaults.circleParameters(
                    backgroundColor = LightBlue
                ),
                lineParameters = LineParametersDefaults.linearGradient(
                    startColor = LightBlue,
                    endColor = Purple
                ),
                contentStartOffset = 16.dp,
                spacer = 32.dp
            ) { modifier -> MessageBubble(modifier, containerColor = LightBlue) }

            TimelineNode(
                position = TimelineNodePosition.MIDDLE,
                circleParameters = CircleParametersDefaults.circleParameters(
                    backgroundColor = Purple
                ),
                contentStartOffset = 16.dp,
                lineParameters = LineParametersDefaults.linearGradient(
                    startColor = Purple,
                    endColor = Coral
                ),
                spacer = 32.dp
            ) { modifier -> MessageBubble(modifier, containerColor = Purple) }

            TimelineNode(
                TimelineNodePosition.LAST,
                circleParameters = CircleParametersDefaults.circleParameters(
                    backgroundColor = LightCoral,
                    stroke = StrokeParameters(color = Coral, width = 2.dp),
                    icon = R.drawable.ic_bubble_warning_16
                ),
                lineParameters = LineParametersDefaults.linearGradient(
                    startColor = Coral,
                    endColor = Coral
                ),
                contentStartOffset = 16.dp,
                spacer = 32.dp
            ) { modifier -> MessageBubble(modifier, containerColor = Coral) }
        }
    }
}

object CircleParametersDefaults {

    private val defaultCircleRadius = 12.dp

    fun circleParameters(
        radius: Dp = defaultCircleRadius,
        backgroundColor: Color = Cyan,
        stroke: StrokeParameters? = null,
        @DrawableRes
        icon: Int? = null
    ) = CircleParameters(
        radius,
        backgroundColor,
        stroke,
        icon
    )
}

object LineParametersDefaults {

    private val defaultLinearGradient = 3.dp

    fun linearGradient(
        strokeWidth: Dp = defaultLinearGradient,
        startColor: Color,
        endColor: Color
    ): LineParameters {
        val brush = Brush.verticalGradient(
            colorStops = arrayOf(
                0.0f to startColor,
                1f to endColor
            )
        )
        return LineParameters(strokeWidth, brush)
    }
}

@Composable
private fun MessageBubble(modifier: Modifier, containerColor: Color) {
    Card(
        modifier = modifier
            .width(200.dp)
            .height(100.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {}
}

