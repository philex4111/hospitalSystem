package utumishiorg.example.app.ui

import utumishiorg.example.app.model.Appointment
import utumishiorg.example.app.model.Doctor
import utumishiorg.example.app.model.Patient
import java.time.format.DateTimeFormatter

private val formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss")

fun printHeader(title: String){
    println("==$title==".trimIndent())
}

fun printPatient(p: Patient){
    println("${p.id} | ${p.name} | Age:" +
            "${p.age} | ${p.diagnosis} | ${p.gender} | " +
            "${p.bloodTypy ?: "N/A"} | ${p.phone} | ${p.email}")
}

fun printDoctor(d: Doctor){
    println(" ${d.id} | ${d.name} | ${d.speciality} |" +
            " ${d.yearsExperience} yrs | ${d.phone} | ${d.email}")
}

fun printAppointment(a: Appointment){
    println(" #${a.id} | patient:${a.patientId} | Dr:${a.doctorId} |" +
            "${a.dateTime.format(formatter)} | ${a.status} | ${a.notes}")
}

fun printListEmpty(itemType: String){
    println("No $itemType found.")
}
