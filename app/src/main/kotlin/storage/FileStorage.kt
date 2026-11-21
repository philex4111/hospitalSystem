package utumishiorg.example.app.storage

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import utumishiorg.example.app.model.Appointment
import utumishiorg.example.app.model.Doctor
import utumishiorg.example.app.model.Patient
import utumishiorg.example.app.service.AppointmentService
import utumishiorg.example.app.service.DoctorService
import utumishiorg.example.app.service.PatientService
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class AppointmentDto(
    val id: Int,
    val patientId: Int,
    val doctorId: Int,
    val dateTime: String,
    val status: String,
    val notes: String
)

class FileStorage(
    private val patientService: PatientService,
    private val doctorService: DoctorService,
    private val appointmentService: AppointmentService
) {
    private val dir = File("data")
    private val patientsFile = File(dir, "patients.json")
    private val doctorsFile = File(dir, "doctors.json")
    private val appointmentsFile = File(dir, "appointments.json")
    private val json = Json { prettyPrint = true }
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    init { dir.mkdirs() }

    fun save() {
        patientsFile.writeText(json.encodeToString(patientService.getAllPatients()))
        doctorsFile.writeText(json.encodeToString(doctorService.getAllDoctors()))
        
        val appts = appointmentService.getAllAppointments().map { a ->
            AppointmentDto(
                id = a.id,
                patientId = a.patientId,
                doctorId = a.doctorId,
                dateTime = a.dateTime.format(formatter),
                status = a.status.name,
                notes = a.notes
            )
        }
        appointmentsFile.writeText(json.encodeToString(appts))
    }

    fun load() {
        if (patientsFile.exists()) {
            val list = json.decodeFromString<List<Patient>>(patientsFile.readText())
            list.forEach { patientService.addPatient(it) }
        }
        if (doctorsFile.exists()) {
            val list = json.decodeFromString<List<Doctor>>(doctorsFile.readText())
            list.forEach { doctorService.addDoctor(it) }
        }
        if (appointmentsFile.exists()) {
            val dtos = json.decodeFromString<List<AppointmentDto>>(appointmentsFile.readText())
            dtos.forEach { dto ->
                appointmentService.scheduleAppointment(
                    dto.patientId, dto.doctorId,
                    LocalDateTime.parse(dto.dateTime, formatter),
                    dto.notes
                )
            }
        }
    }
}