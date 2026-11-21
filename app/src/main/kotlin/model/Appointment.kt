package utumishiorg.example.app.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import utumishiorg.example.app.model.enums.AppointmentStatus
import java.time.LocalDateTime
import java.time.Duration as JavaDuration
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.OptIn
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toDuration

@OptIn(ExperimentalTime::class)
@Serializable
data class Appointment(
    val id: Int,
    val patientId: Int,
    val doctorId: Int,
    @Contextual val dateTime: LocalDateTime,
    val status: AppointmentStatus = AppointmentStatus.SCHEDULED,
    val notes: String = "",
    val slotDuration: Duration = 30.minutes
) {
    init {
        require(id >= 0) { "Id must be non-negative" }
        require(patientId > 0) { "Patient id must be positive" }
        require(doctorId > 0) { "Doctor id must be positive" }
    }

    // ─── Overlap Logic ───
    
    val startTime: LocalDateTime get() = dateTime
    val endTime: LocalDateTime get() = dateTime.plus(toJavaDuration(slotDuration))

    fun overlaps(other: Appointment): Boolean {
        return this.startTime < other.endTime && other.startTime < this.endTime
    }

    fun overlaps(start: LocalDateTime, end: LocalDateTime): Boolean {
        return this.startTime < end && start < this.endTime
    }

    private fun toJavaDuration(d: Duration): JavaDuration {
        return JavaDuration.ofMillis(d.inWholeMilliseconds)
    }
}