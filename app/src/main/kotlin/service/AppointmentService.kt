package utumishiorg.example.app.service

import kotlinx.serialization.Serializable
import utumishiorg.example.app.model.Appointment
import utumishiorg.example.app.model.enums.AppointmentStatus
import utumishiorg.example.app.service.PatientService
import utumishiorg.example.app.service.DoctorService
import java.time.LocalDateTime
import kotlin.Result

class AppointmentService(
    private val patientService: PatientService,
    private val doctorService: DoctorService
) {
    private val appointments = mutableListOf<Appointment>()
    private var nextId = 1 // Auto-increment id

    // -- CREATE --

    fun scheduleAppointment(
        patientId: Int,
        doctorId: Int,
        dateTime: LocalDateTime,
        notes: String = ""
    ): Result<Appointment> {
        // check if patient exists
        if (patientService.findById(patientId) == null) {
            return Result.failure(IllegalArgumentException("Patient with ID $patientId not found"))
        }
        // check if doctor exists
        if (doctorService.findById(doctorId) == null) {
            return Result.failure(IllegalArgumentException("Doctor with ID $doctorId not found"))
        }
        // validate future appointment (in service layer for test control)
        if (dateTime.isBefore(LocalDateTime.now().minusMinutes(1))) {
            return Result.failure(IllegalArgumentException("Cannot schedule appointment in the past"))
        }
        // check for conflicts using overlap logic (30-minute slots)
        val newAppointment = Appointment(
            id = 0, // temp, will be set after
            patientId = patientId,
            doctorId = doctorId,
            dateTime = dateTime,
            notes = notes
        )
        val hasConflict = appointments.any { existing ->
            existing.doctorId == doctorId &&
            existing.status != AppointmentStatus.CANCELLED &&
            existing.overlaps(newAppointment)
        }
        if (hasConflict) {
            return Result.failure(IllegalArgumentException("Doctor already has an appointment at this time"))
        }
        val appointment = Appointment(
            id = nextId++,
            patientId = patientId,
            doctorId = doctorId,
            dateTime = dateTime,
            status = AppointmentStatus.SCHEDULED,
            notes = notes
        )
        appointments.add(appointment)
        return Result.success(appointment)
    }

    // -- READ --

    fun getAllAppointments(): List<Appointment> = appointments.toList()

    fun findById(id: Int): Appointment? = appointments.find { it.id == id }

    fun findByPatientId(patientId: Int): List<Appointment> =
        appointments.filter { it.patientId == patientId }

    fun findByDoctorId(doctorId: Int): List<Appointment> =
        appointments.filter { it.doctorId == doctorId }

    fun findByDate(dateTime: LocalDateTime): List<Appointment> =
        appointments.filter { it.dateTime.toLocalDate() == dateTime.toLocalDate() }

    fun findByStatus(status: AppointmentStatus): List<Appointment> =
        appointments.filter { it.status == status }

    // -- UPDATE --

    fun completeAppointment(id: Int): Boolean {
        val appointment = findById(id) ?: return false
        val updated = appointment.copy(status = AppointmentStatus.COMPLETED)
        val index = appointments.indexOfFirst { it.id == id }
        appointments[index] = updated
        return true
    }

    fun cancelAppointment(id: Int): Boolean {
        val appointment = findById(id) ?: return false
        val updated = appointment.copy(status = AppointmentStatus.CANCELLED)
        val index = appointments.indexOfFirst { it.id == id }
        appointments[index] = updated
        return true
    }

    fun markNoShow(id: Int): Boolean {
        val appointment = findById(id) ?: return false
        val updated = appointment.copy(status = AppointmentStatus.NO_SHOW)
        val index = appointments.indexOfFirst { it.id == id }
        appointments[index] = updated
        return true
    }

    fun rescheduleAppointment(id: Int, newDateTime: LocalDateTime): Result<Appointment> {
        val appointment = findById(id) ?: 
            return Result.failure(IllegalArgumentException("Appointment with ID $id not found"))
        if (newDateTime.isBefore(LocalDateTime.now().minusMinutes(1))) {
            return Result.failure(IllegalArgumentException("New date and time can not be in the past"))
        }
        // Create a temporary appointment with the new time to check conflicts
        val tempAppointment = appointment.copy(dateTime = newDateTime)
        val hasConflict = appointments.any { existing ->
            existing.id != id &&
            existing.doctorId == appointment.doctorId &&
            existing.status != AppointmentStatus.CANCELLED &&
            existing.overlaps(tempAppointment)
        }
        if (hasConflict) {
            return Result.failure(IllegalArgumentException("Doctor already has an appointment at this time"))
        }
        val updated = appointment.copy(dateTime = newDateTime)
        val index = appointments.indexOfFirst { it.id == id }
        appointments[index] = updated
        return Result.success(updated)
    }

    // -- DELETE --

    fun removeAppointment(id: Int): Boolean {
        val appointment = findById(id) ?: return false
        appointments.remove(appointment)
        return true
    }
}