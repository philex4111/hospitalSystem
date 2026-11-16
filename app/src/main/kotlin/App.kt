package utumishiorg.example.app

import utumishiorg.example.app.service.PatientService
import utumishiorg.example.app.service.DoctorService
import utumishiorg.example.app.service.AppointmentService
import utumishiorg.example.app.ui.ConsoleMenu

fun main(){
    val patientService = PatientService()
    val doctorService = DoctorService()
    val appointmentService = AppointmentService()
    val menu = ConsoleMenu(patientService, doctorService, appointmentService)
    menu.start()
}