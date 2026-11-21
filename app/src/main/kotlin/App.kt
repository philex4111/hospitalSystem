package utumishiorg.example.app

import utumishiorg.example.app.service.PatientService
import utumishiorg.example.app.service.DoctorService
import utumishiorg.example.app.service.AppointmentService
import utumishiorg.example.app.ui.ConsoleMenu
import utumishiorg.example.app.storage.FileStorage

fun main(){
    val patientService = PatientService()
    val doctorService = DoctorService()
    val appointmentService = AppointmentService(patientService, doctorService)

    val storage = FileStorage(patientService, doctorService, appointmentService)

    storage.load()

    val menu = ConsoleMenu(patientService, doctorService, appointmentService)
    menu.start()

    storage.save()
}