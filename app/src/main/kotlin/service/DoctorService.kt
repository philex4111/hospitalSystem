package utumishiorg.example.app.service

import kotlin.Result
import utumishiorg.example.app.model.Doctor

class DoctorService {
    private val doctors = mutableListOf<Doctor>()

    // -- CREATE --
    fun addDoctor(doctor: Doctor): Result<Doctor> {
        if (doctors.any { it.id == doctor.id }) {
            return Result.failure(IllegalArgumentException("Doctor with ID ${doctor.id} already exists"))
        }
        doctors.add(doctor)
        return Result.success(doctor)
    }

    // -- READ --
    fun getAllDoctors(): List<Doctor> = doctors.toList()

    fun getDoctorCount(): Int = doctors.size

    fun findById(id: Int): Doctor? = doctors.find { it.id == id }

    fun findBySpecialty(specialty: String): List<Doctor> =
        doctors.filter { it.specialty.contains(specialty, ignoreCase = true) }

    fun findByName(name: String): List<Doctor> =
        doctors.filter { it.name.contains(name, ignoreCase = true) }

    // -- UPDATE --
    fun updateDoctor(id: Int, updated: Doctor): Result<Doctor> {
        val index = doctors.indexOfFirst { it.id == id }
      if (index < 0) {
          return Result.failure(IllegalArgumentException("Doctor with ID ${updated.id} not found"))
      }
        if (updated.id != id && doctors.any(){it.id == updated.id}){
            return Result.failure(IllegalArgumentException("Doctor with ID ${updated.id} already exists"))
        }
        doctors[index] = updated
        return Result.success(updated)
    }

    // -- DELETE --
    fun removeDoctor(id: Int): Boolean {
        val doctor = findById(id)
        return if (doctor != null) {
            doctors.remove(doctor)
            true
        } else false
    }
}