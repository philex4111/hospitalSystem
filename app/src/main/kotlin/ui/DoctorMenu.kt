package utumishiorg.example.app.ui

import jdk.jfr.internal.consumer.EventLog.update
import utumishiorg.example.app.model.Doctor
import utumishiorg.example.app.service.DoctorService
import java.nio.file.Files.delete

class DoctorMenu(
    private val service: DoctorService
){
    fun show(){
        println("\n--- Doctors ---")
        println("1. Add Doctor")
        println("2. View All Doctors")
        println("3. Search By ID")
        println("4. Search By Name")
        println("5. search by speciality")
        println("6. update Doctor")
        println("7. Delete Doctor")
        println("0. Back")

        when(readInt("Choose: ")){
            1 -> addDoctor()
            2 -> viewAll()
            3 -> searchById()
            4 -> searchByName()
            5 -> searchBySpeciality()
            6 -> update()
            7 -> delete()
            else -> println("Invalid option")
        }
    }
    private fun addDoctor(){
        printHeader("Add Doctor")
        val id = readInt("ID: ")
        val name = readString("Name: ")
        val speciality = readString("Speciality: ")
        val experience = readInt("Years of experience: ")
        val phone = readString("phone (optional): ")
        val email = readString("Email (optional): ")
        val doctor = Doctor(id, name, speciality, experience, phone, email)
       if (service.addDoctor(doctor))
           println("Doctor added")
        else println("Error: ID $id already exists")
    }

    private fun viewAll(){
        printHeader("All Doctors")
        val list = service.getAllDoctors()
        if(list.isEmpty())
            printListEmpty("doctors")
        else list.forEach { printDoctor(it) }
    }

    private fun searchById(){
        val id =readInt("Enter ID: ")
        service.findById(id)?.let {
            printDoctor(it)
        }?:
        println(" $id Not found")
    }

    fun searchByName(){
        val name= readString("Enter Name: ")
        val results= service.findByName(name)
        if(results.isEmpty())
            printListEmpty("doctors")
        else results.forEach { printDoctor(it) }
    }

    private fun searchBySpeciality(){
        val speciality = readString("Enter speciality: ")
        val results = service.findBySpeciality((speciality))
        if(results.isEmpty())
            printListEmpty("doctors")
        else results.forEach { printDoctor(it) }
    }

    private fun update(){
        val id = readInt("Enter ID to update: ")
        service.findById(id) ?: return println("Not found")
        println("Enter new values: ")
        val name = readString("Name: ")
        val speciality = readString("speciality: ")
        val experience = readInt("Years of experience:")
        val phone = readString("phone (optional): ")
        val email = readString("Email (optional): ")
        val doctor = Doctor(id, name, speciality, experience, phone,email)

        if (service.updateDoctor(id, doctor))
            println("updated")
        else println("Failed.")
    }

    private fun delete(){
        val id = readInt("Enter ID to delete: ")
        if(service.removeDoctor(id))
            println("Deleted")
        else println("User not found")
    }
}