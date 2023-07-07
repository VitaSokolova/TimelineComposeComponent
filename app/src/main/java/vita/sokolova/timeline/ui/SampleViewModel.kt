package vita.sokolova.timeline.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import vita.sokolova.timeline.domain.entities.HiringStage
import vita.sokolova.timeline.domain.entities.HiringStageStatus
import vita.sokolova.timeline.domain.entities.MessageSender
import java.time.LocalDate

class SampleViewModel : ViewModel() {

    val hiringProcessState: Flow<Array<HiringStage>> = flowOf(
        TEST_DATA
    )

    companion object {
        val TEST_DATA = arrayOf(
            HiringStage(
                date = LocalDate.now(),
                initiator = MessageSender.Candidate(
                    "VS",
                    "Hi! I will be glad to join DreamCompany team. I've sent you my CV."
                ),
                status = HiringStageStatus.FINISHED
            ),
            HiringStage(
                date = LocalDate.now(),
                initiator = MessageSender.HR(
                    "JD",
                    "Hi! Let's have a short call to discuss your expectations and experience."
                ),
                status = HiringStageStatus.FINISHED
            ),
            HiringStage(
                date = LocalDate.now().plusDays(1),
                initiator = MessageSender.System("Screening call with Jane Doe."),
                status = HiringStageStatus.FINISHED
            ),
            HiringStage(
                date = LocalDate.now().plusDays(1),
                initiator = MessageSender.System("We are waiting for your test task. It should be completed at least one day before the technical interview."),
                status = HiringStageStatus.CURRENT
            ),
            HiringStage(
                date = LocalDate.now().plusDays(7),
                initiator = MessageSender.System("Technical interview."),
                status = HiringStageStatus.UPCOMING
            ),
            HiringStage(
                date = null,
                initiator = MessageSender.System("Bar raiser interview with the team."),
                status = HiringStageStatus.UPCOMING
            ),
            HiringStage(
                date = null,
                initiator = MessageSender.System("Offer proposal."),
                status = HiringStageStatus.UPCOMING
            )
        )
    }
}
