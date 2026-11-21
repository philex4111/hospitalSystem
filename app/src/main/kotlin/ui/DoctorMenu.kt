package utumishiorg.example.app.ui

import utumishiorg.example.app.model.Doctor
import utumishiorg.example.app.service.DoctorService

class DoctorMenu(
    private val service: DoctorService
) {
    fun show() {
        println("\n--- Doctors ---")
        println("1. Add Doctor")
        println("2. View All Doctors")
        println("3. Search By ID")
        println("4. Search By Name")
        println("5. Search by Specialty")
        println("6. Update Doctor")
        println("7. Delete Doctor")
        println("0. Back")

        when (readInt("Choose: ")) {
            1 -> addDoctor()
            2 -> viewAll()
            3 -> searchById()
            4 -> searchByName()
            5 -> searchBySpecialty()
            6 -> update()
            7 -> delete()
            0 -> {}
            else -> println("Invalid option")
        }
    }

    private fun addDoctor() {
        printHeader("Add Doctor")
        val id: Int
        while (true) {
            val inputId = readInt("ID: ")
            if (service.findById(inputId) == null) {
                id = inputId
                break
            }
            println("Error: Doctor with ID $inputId already exists. Try another")
        }
        val name = readNonEmptyString("Name: ",{it.matches(Regex("[A-Za-z\\s'-]+$"))},
            "invalid name. Try again")
        val specialty = readString("Specialty: ")
        val experience = readIntInRange("Years of experience: ", 0, 100)
        val phone: String
        while (true) {
            val input = readOptionalString("Phone (optional): ")
            if (input.isBlank()){
                phone= ""
                break
            }
            if (input.length == 10 && input.all { it.isDigit() }){
                phone = input
                break
            }
            println("Invalid phone number. Try again")
        }
        val email: String
        while (true) {
            val input = readOptionalString("Email (optional): ")
            if (input.isBlank()){
                email= ""
                break
            }
            if (input.contains("@") && input.contains(".")){
                email = input
                break
            }
            println("Invalid email format. Try again")
        }
        val doctor = Doctor(id, name, specialty, experience, phone, email)
        service.addDoctor(doctor).onFailure {
            println("Error: ${it.message}")
        }.getOrNull()?.let {
            println("Doctor added")
        }
    }

    private fun viewAll() {
        printHeader("All Doctors")
        val list = service.getAllDoctors()
        if (list.isEmpty())
            printListEmpty("doctors")
        else list.forEach { printDoctor(it) }
    }

    private fun searchById() {
        val id = readInt("Enter ID: ")
        service.findById(id)?.let {
            printDoctor(it)
        } ?: println(" $id Not found")
    }

    private fun searchByName() {
        val name = readString("Enter Name: ")
        val results = service.findByName(name)
        if (results.isEmpty())
            printListEmpty("doctors")
        else results.forEach { printDoctor(it) }
    }

    private fun searchBySpecialty() {
        val specialty = readString("Enter specialty: ")
        val results = service.findBySpecialty(specialty)
        if (results.isEmpty())
            printListEmpty("doctors")
        else results.forEach { printDoctor(it) }
    }

    private fun update() {
        val id = readInt("Enter ID to update: ")
        service.findById(id) ?: return println("Not found")
        println("Enter new values: ")
        val name = readNonEmptyString("Name: ",{it.matches(Regex("[A-Za-z\\s'-]+$"))},
            "invalid name. Try again")
        val specialty = readString("Specialty: ")
        val experience = readIntInRange("Years of experience: ", 0, 100)
        val phone = readOptionalString("Phone (optional): ")
        val email = readOptionalString("Email (optional): ")
        val doctor = Doctor(id, name, specialty, experience, phone, email)
        service.updateDoctor(id, doctor).onFailure {
            println("Error: ${it.message}")
        }.getOrNull()?.let {
            println("Updated")
        }
    }

    private fun delete() {
        val id = readInt("Enter ID to delete: ")
        if (service.removeDoctor(id))
            println("Deleted")
        else println("User not found")
    }
}