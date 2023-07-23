package vita.sokolova.example.domain.entities

import java.time.LocalDate

data class HiringStage(
    val date: LocalDate?,
    val initiator: MessageSender,
    val status: HiringStageStatus
)

