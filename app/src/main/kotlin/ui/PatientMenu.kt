package utumishiorg.example.app.ui

import utumishiorg.example.app.model.Patient
import utumishiorg.example.app.model.enums.BloodType
import utumishiorg.example.app.model.enums.Gender
import utumishiorg.example.app.service.PatientService
import java.nio.file.Files.delete
import java.security.Provider

class PatientMenu(
    private val service: PatientService
) {
    fun show() {
        println("\n--- PATIENTS ---")
        println("1. Add Patient")
        println("2. View All Patients")
        println("3. search by ID")
        println("4. Search by name")
        println("5. Search by Diagnosis")
        println("6. Search by Gender")
        println("7. Search by Age Range")
        println("8. Update Patient")
        println("9. Delete Patient")
        println("0. Back")

        when (readInt("Choose: ")) {
            1 -> addPatient()
            2 -> viewAll()
            3 -> searchById()
            4 -> searchByName()
            5 -> searchByDiagnosis()
            6 -> searchByGender()
            7 -> searchByAgeRange()
            8 -> update()
            9 -> delete()
            else -> println("Invalid option")
        }
    }

    private fun addPatient() {
        printHeader("Add Patient")
        val id = readInt("ID: ")
        val name = readString("Name: ")
        val age = readInt("Age: ")
        val diagnosis = readString("Diagnosis: ")

        println("Gender: 1=MALE, 2=FEMALE, 3=OTHER")
        val gender = when (readInt("Choose: ")) {
            1 -> Gender.MALE
            2 -> Gender.FEMALE
            else -> Gender.OTHER
        }

        println("Blood Type: 1=A+ 2=A- 3=B+ 4=B- 5=O+ 6=O- 7=AB+ 8=AB- 0=Skip")
        val bloodType = when (readInt("Choose: ")) {
            1 -> BloodType.A_POSITIVE
            2 -> BloodType.A_NEGATIVE
            3 -> BloodType.B_POSITIVE
            4 -> BloodType.B_NEGATIVE
            5 -> BloodType.O_POSITIVE
            6 -> BloodType.O_NEGATIVE
            7 -> BloodType.AB_POSITIVE
            8 -> BloodType.AB_NEGATIVE
            else -> null
        }

        val phone = readString("Phone: (optional): ")
        val email = readString("Email: (optional): ")

        val patient = Patient(id, name, age, diagnosis, gender, bloodType, phone, email)

        if (service.addPatient(patient))
            println("Patient added")
        else println("Error: ID $id already exists")
    }

    //--VIEW ALL--
    private fun viewAll() {
        printHeader("All Patients")
        val list = service.getAllPatients()
        if (list.isEmpty())
            printListEmpty("patients")
        else list.forEach { printPatient(it) }
    }

    //--SEARCH--
    private fun searchById() {
        val id = readInt("Enter ID: ")
        service.findById(id)?.let { printPatient(it) } ?: println("Not found")
    }

    private fun searchByName() {
        val name = readString("Enter name: ")
        val result = service.findByName(name)
        if (result.isEmpty())
            printListEmpty("Patients")
        else result.forEach { printPatient(it) }
    }

    private fun searchByDiagnosis() {
        val diagnosis = readString("Enter diagnosis: ")
        val results = service.findByDiagnosis(diagnosis)
        if (results.isEmpty())
            printListEmpty("Patients")
        else results.forEach { printPatient(it) }
    }

    private fun searchByGender() {
        println("Gender: 1=MALE, 2=FEMALE, 3=OTHER")
        val gender = when (readInt("Choose: ")) {
            1 -> Gender.MALE
            2 -> Gender.FEMALE
            else -> Gender.OTHER
        }
        val results =service.findByGender(gender)
        if(results.isEmpty())
            printListEmpty("Patients")
        else results.forEach { printPatient(it) }
    }

    private fun searchByAgeRange(){
        val min = readInt("Min age: ")
        val max = readInt("Max age : ")
        val results = service.findByAgeRange(min, max)
        if(results.isEmpty())
            printListEmpty("Patients")
        else results.forEach { printPatient(it) }
    }

    private fun update() {
        val id = readInt("Enter ID to update: ")
        service.findById(id) ?: return println("Not found")
        println("Enter new values: ")
        val name = readString("Name: ")
        val age = readInt("Age:")
        val diagnosis = readString("Diagnosis: ")
        val update = Patient(id, name, age, diagnosis)
        if (service.updatePatient(id, update))
            println("updated")
        else println("Failed.")
    }

    private fun delete(){
        val id = readInt("Enter ID to delete: ")
        if(service.removePatient(id))
            if (service.removePatient(id))
                println("Deleted")
        else println("Not found.")
    }
}