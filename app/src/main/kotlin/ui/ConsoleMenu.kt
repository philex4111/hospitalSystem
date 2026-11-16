package utumishiorg.example.app.ui

import utumishiorg.example.app.service.AppointmentService
import utumishiorg.example.app.service.DoctorService
import utumishiorg.example.app.service.PatientService

class ConsoleMenu(
    private val patientService: PatientService,
    private val doctorService: DoctorService,
    private val appointmentService: AppointmentService
){

    private val patientMenue = PatientMenu(patientService)
    private val doctorMenue = DoctorMenu(doctorService)
    private val appointmentMenu = AppointmentMenu(appointmentService, patientService, doctorService)

    fun start(){
        printHeader("HOSPITAL MANAGEMENT")
        var running = true
        while (running){
            println("""
                |1. patient
                |2. Doctor
                |3. Appointment
                |4. Summary
                |0. Exit
            """.trimIndent())

            val choice = readInt("Choose:")
            running = when(choice){
                1 -> {patientMenue.show(); true}
                2 -> {doctorMenue.show(); true}
                3 -> {appointmentMenu.show(); true}
                4 -> {printSummary(); true}
                0 -> {println("Goodbye!"); false}
                else -> {println("Invalid option"); true}

            }
        }
    }

    private fun printSummary(){
        println("""
            Patients: ${patientService.getPatientCount()}
            Doctors: ${doctorService.getDoctorCount()}
            Appointments: ${appointmentService.getAllAppointments()}
        """.trimIndent())
    }
}