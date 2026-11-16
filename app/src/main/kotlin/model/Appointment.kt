package utumishiorg.example.app.model

import utumishiorg.example.app.model.enums.AppointmentStatus
import java.time.LocalDateTime

data class Appointment(
    val id: Int,
    val patientId: Int,
    val doctorId: Int,
    val dateTime: LocalDateTime,
    val status: AppointmentStatus = AppointmentStatus.SCHEDULED,
    val notes: String = ""
)