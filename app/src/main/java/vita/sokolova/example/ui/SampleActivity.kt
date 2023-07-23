package vita.sokolova.example.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import vita.sokolova.example.ui.SampleViewModel.Companion.TEST_DATA
import vita.sokolova.example.domain.entities.HiringStage
import vita.sokolova.example.ui.composables.LazyTimeline
import vita.sokolova.example.ui.theme.TimelineComposeComponentTheme

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
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyTimeline(timelineStages)
    }
}

@Preview(showBackground = true)
@Composable
private fun TimelineUsagePreview() {
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


