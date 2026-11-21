package utumishiorg.example.app.service

import utumishiorg.example.app.model.Appointment
import utumishiorg.example.app.model.Patient
import utumishiorg.example.app.model.Doctor
import utumishiorg.example.app.model.enums.AppointmentStatus
import java.time.LocalDateTime
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AppointmentServiceTest {
    private lateinit var patientService: PatientService
    private lateinit var doctorService: DoctorService
    private lateinit var appointmentService: AppointmentService

    @BeforeEach
    fun setup() {
        patientService = PatientService()
        doctorService = DoctorService()
        appointmentService = AppointmentService(patientService, doctorService)

        // Add test patient and doctor
        patientService.addPatient(Patient(1, "Liam Pyne", 30, "Flu"))
        doctorService.addDoctor(Doctor(1, "Dr. Smith", "Cardiology", 10))
    }

    // Use MAX date minus some time to ensure it's always in the future
    private val FUTURE_BASE = LocalDateTime.MAX.minusYears(1)

    private fun futureTime(hours: Long = 0, minutes: Long = 0): LocalDateTime {
        return FUTURE_BASE.plusHours(hours).plusMinutes(minutes)
    }

    @Test
    fun `scheduleAppointment succeeds with valid data`() {
        val future = futureTime()
        val result = appointmentService.scheduleAppointment(1, 1, future, "checkup")
        assertTrue(result.isSuccess)
        assertEquals(1, appointmentService.getAllAppointments().size)
    }

    @Test
    fun `scheduleAppointment fails when patient not found`() {
        val future = futureTime()
        val result = appointmentService.scheduleAppointment(999, 1, future)
        assertTrue(result.isFailure)
        assertEquals("Patient with ID 999 not found", result.exceptionOrNull()?.message)
    }

    @Test
    fun `schedule appointment fails when doctor not found`() {
        val future = futureTime()
        val result = appointmentService.scheduleAppointment(1, 999, future)
        assertTrue(result.isFailure)
        assertEquals("Doctor with ID 999 not found", result.exceptionOrNull()?.message)
    }

    @Test
    fun `scheduleAppointment fails for past date`() {
        val past = LocalDateTime.now().minusHours(1)
        val result = appointmentService.scheduleAppointment(1, 1, past)
        assertTrue(result.isFailure)
        assertEquals("Cannot schedule appointment in the past", result.exceptionOrNull()?.message)
    }

    @Test
    fun `scheduleAppointment fails on double booking`() {
        val future = futureTime()
        appointmentService.scheduleAppointment(1, 1, future) // 1st booking
        val result = appointmentService.scheduleAppointment(1, 1, future) // same time - conflict
        assertTrue(result.isFailure)
        assertEquals("Doctor already has an appointment at this time", result.exceptionOrNull()?.message)
    }

    @Test
    fun `scheduleAppointment fails on overlapping booking`() {
        val base = futureTime()
        appointmentService.scheduleAppointment(1, 1, base) // 10:00-10:30
        val result = appointmentService.scheduleAppointment(1, 1, base.plusMinutes(15)) // 10:15-10:45 overlaps
        assertTrue(result.isFailure)
        assertEquals("Doctor already has an appointment at this time", result.exceptionOrNull()?.message)
    }

    @Test
    fun `scheduleAppointment succeeds with non-overlapping times`() {
        val base = futureTime()
        appointmentService.scheduleAppointment(1, 1, base) // 10:00-10:30
        val result = appointmentService.scheduleAppointment(1, 1, base.plusMinutes(30)) // 10:30-11:00 no overlap
        assertTrue(result.isSuccess)
    }

    @Test
    fun `rescheduleAppointment fails on conflict`() {
        val base = futureTime()
        val future2 = base.plusHours(1)
        val appt1 = appointmentService.scheduleAppointment(1, 1, base).getOrThrow() // 10:00-10:30
        appointmentService.scheduleAppointment(1, 1, future2) // 11:00-11:30
        // Try to reschedule appt1 to overlap with future2
        val result = appointmentService.rescheduleAppointment(appt1.id, future2)
        assertTrue(result.isFailure)
        assertEquals("Doctor already has an appointment at this time", result.exceptionOrNull()?.message)
    }

    @Test
    fun `rescheduleAppointment succeeds when no conflict`() {
        val base = futureTime()
        val future2 = base.plusHours(1)
        val appt1 = appointmentService.scheduleAppointment(1, 1, base).getOrThrow()
        // Reschedule to a free time
        val result = appointmentService.rescheduleAppointment(appt1.id, future2)
        assertTrue(result.isSuccess)
    }

    @Test
    fun `completeAppointment changes status`() {
        val future = futureTime()
        val appt = appointmentService.scheduleAppointment(1, 1, future).getOrThrow()
        val completed = appointmentService.completeAppointment(appt.id)
        assertTrue(completed)
        assertEquals(AppointmentStatus.COMPLETED, appointmentService.findById(appt.id)?.status)
    }
}