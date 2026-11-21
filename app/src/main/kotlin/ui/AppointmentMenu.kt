package utumishiorg.example.app.ui

import utumishiorg.example.app.model.Appointment
import  utumishiorg.example.app.model.enums.AppointmentStatus
import utumishiorg.example.app.service.AppointmentService
import utumishiorg.example.app.service.DoctorService
import utumishiorg.example.app.service.PatientService
import java.nio.file.Files.delete
import java.time.LocalDateTime

class AppointmentMenu(
    private val service: AppointmentService,
    private val patientService: PatientService,
    private val doctorService: DoctorService
){
    fun show(){
        println("\n--- APPOINTMENT ---")
        println("1. Schedule Appointment")
        println("2. View All Appointments")
        println("3. View by Patient")
        println("4. View by Doctor")
        println("5. View by Date")
        println("6. view by Status")
        println("7. Complete Appointment")
        println("8. Cancel Appointment")
        println("9. Mark No-Show")
        println("10. Reschedule Appointment")
        println("11. Delete Appointment")
        println("0. Back")

        when(readInt("Choose: ")){
            1 -> schedule()
            2 -> viewAll()
            3 -> viewByPatient()
            4 -> viewByDoctor()
            5 -> viewByDate()
            6 -> viewByStatus()
            7 -> complete()
            8 -> cancel()
            9 -> markNoShow()
            10 -> reschedule()
            11 -> delete()
            0 -> {}
            else -> println("Invalid option")
        }
    }

    private fun schedule(){
        printHeader("Schedule Appointment")
        val patientId = readInt("Patient ID: ")
        if (patientService.findById(patientId) == null){
            println("patient not found")
            return
        }
        val doctorId = readInt("Doctor ID: ")
        if (doctorService.findById(doctorId)== null){
            println("Doctor not found")
            return
        }
        val dateTime = readDateTime("Date & Time (YYYY-MM-DD HH:MM): ")?: return
        val notes = readString("Notes (optional): ")

        service.scheduleAppointment(patientId, doctorId, dateTime, notes).onFailure { println("Error: ${it.message}") }.getOrNull()?.let {
            appointment -> println("Appointment scheduled for ${appointment.id}")
        }
    }

    private fun viewAll(){
        printHeader("All Appointments")
        val list = service.getAllAppointments()
        if(list.isEmpty())
            printListEmpty("appointments")
        else list.forEach { printAppointment(it) }
    }

    private fun viewByPatient(){
        val patientId = readInt("Enter patient Id: ")
        val result = service.findByPatientId(patientId)
        if (result.isEmpty())
            printListEmpty("appointments")
        else result.forEach { printAppointment(it) }
    }

    private fun viewByDoctor(){
        val doctorId = readInt("Enter doctor id: ")
        val result = service.findByDoctorId(doctorId)
        if (result.isEmpty())
            printListEmpty("appointments")
        else result.forEach { printAppointment(it) }
    }

    private fun viewByDate(){
        val date = readDateTime("Enter date (yyyy-mm-dd): ")?:
        return
        val result = service.findByDate(date)
        if (result.isEmpty())
            printListEmpty("appointments")
        else result.forEach { printAppointment(it) }
    }

    private fun viewByStatus(){
        println("1= SCHEDULED, 2= COMPLETED, 3= CANCELLED, 4= NO-SHOW")
        val status = when(readInt("Choose: ")){
            1 -> AppointmentStatus.SCHEDULED
            2 -> AppointmentStatus.COMPLETED
            3 -> AppointmentStatus.CANCELLED
            else ->  AppointmentStatus.NO_SHOW
        }
        val result = service.findByStatus(status)
        if(result.isEmpty())
            printListEmpty("appointments")
        else result.forEach { printAppointment(it) }
    }

    private fun complete(){
        val id = readInt("Appointment ID: ")
        if (service.completeAppointment(id))
            println("Marked as completed")
        else println("Not found")
    }

    private fun cancel(){
        val id = readInt("Appointment ID: ")
        if (service.cancelAppointment(id))
            println("Marked as cancelled")
        else println("Not found")
    }

    private fun markNoShow(){
        val id = readInt("Enter ID: ")
        if (service.markNoShow(id))
            println("Marked as no-show")
        else println("Not found")
    }

    private fun reschedule(){
     val id = readInt("Enter ID: ")
        val newDateTime = readDateTime("Enter new date & time (YYYY-MM-DD HH:MM): ")?:
        return
        service.rescheduleAppointment(id, newDateTime).onFailure { println("Error: ${it.message}") }.getOrNull()?.let {
            println("Rescheduled")
        }
    }

    private fun delete(){
        val id = readInt("Appointment ID: ")
        if (service.removeAppointment(id))
            println("Deleted")
        else println("Not found")
    }
}