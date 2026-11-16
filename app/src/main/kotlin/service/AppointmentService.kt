package utumishiorg.example.app.service

import utumishiorg.example.app.model.Appointment
import utumishiorg.example.app.model.enums.AppointmentStatus
import java.time.LocalDateTime


class AppointmentService{
    private val appointments = mutableListOf<Appointment>()
    private var nextId = 1 // Auto-increment id

            //--CREATE--

    fun scheduleAppointment(
        patientId: Int,
        doctorId: Int,
        dateTime: LocalDateTime,
        notes: String = ""
    ): Appointment{
        val appointment = Appointment(
            id = nextId++,
            patientId = patientId,
            doctorId = doctorId,
            dateTime = dateTime,
            status = AppointmentStatus.SCHEDULED,
            notes = notes
        )
        appointments.add(appointment)
        return appointment
    }

            //--READ--

    fun getAllAppointments(): List<Appointment> = appointments.toList()

    fun findById(id: Int): Appointment? = appointments.find { it.id == id  }

    fun findByPatientId(patientId: Int): List<Appointment> =
        appointments.filter { it.patientId == patientId }

    fun findByDoctorId(doctorId: Int): List<Appointment> =
        appointments.filter { it.doctorId == doctorId }

    fun findByDate(dateTime: LocalDateTime): List<Appointment> =
        appointments.filter{it.dateTime.toLocalDate() == dateTime.toLocalDate()}

    fun findByStatus(status: AppointmentStatus): List<Appointment> =
        appointments.filter { it.status ==status }

                //--UPDATE--

    fun completeAppointment(id: Int): Boolean{
        val appointment = findById(id)
        if (appointment != null){
            val updated = appointment.copy(status = AppointmentStatus.COMPLETED)
            val index = appointments.indexOfFirst { it.id == id }
            appointments[index] = updated
            return true
        }
        return false
    }

    fun cancelAppointment(id: Int): Boolean{
        val appointment = findById(id)
        if (appointment != null){
            val updated = appointment.copy(status = AppointmentStatus.CANCELLED)
            val index = appointments.indexOfFirst { it.id == id }
            appointments[index] = updated
            return true
        }
        return false
    }

    fun markNoShow(id: Int): Boolean{
        val appointment = findById(id)
        if (appointment != null){
            val updated = appointment.copy(status = AppointmentStatus.NO_SHOW)
            val index = appointments.indexOfFirst { it.id == id }
            appointments[index] = updated
            return true
        }
        return false
    }

    fun rescheduleAppointment(id: Int, newDateTime: LocalDateTime): Boolean{
        val appointment = findById(id)
        if (appointment != null){
            val updated = appointment.copy(dateTime = newDateTime)
            val index = appointments.indexOfFirst { it.id == id }
            appointments[index] = updated
            return true
        }
        return false
    }

            //--DELETE--

    fun removeAppointment(id: Int): Boolean{
        val appointment = findById(id)
        return if (appointment != null){
            appointments.remove(appointment)
            true
        }else false
    }

}