package vita.sokolova.timeline.domain

import java.time.LocalDate

data class HiringStage(
    val date: LocalDate?,
    val initiator: MessageSender,
    val status: HiringStageStatus
)

