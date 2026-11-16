package utumishiorg.example.app.service

import utumishiorg.example.app.model.Doctor

class DoctorService{
    private val doctors = mutableListOf<Doctor>()

                //--CREATE--

    fun addDoctor(doctor: Doctor): Boolean{
        if (doctors.any {it.id == doctor.id}){
            return false
        }
        doctors.add(doctor)
        return true
    }

                //--READ--

    fun getAllDoctors(): List<Doctor> = doctors.toList()

    fun getDoctorCount(): Int = doctors.size

    fun findById(id: Int): Doctor? = doctors.find {it.id == id}

    fun findBySpeciality(speciality: String): List<Doctor> =
        doctors.filter { it.speciality.contains(speciality, ignoreCase = true) }

    fun findByName(name: String): List<Doctor> =
        doctors.filter { it.name.contains(name, ignoreCase = true) }

                //--UPDATE--

    fun updateDoctor(id: Int, update: Doctor): Boolean{
        val index = doctors.indexOfFirst { it.id == id }
        if (index >= 0){
            doctors[index] = update
            return true
        }
        return false
    }
                //--DELETE--

    fun removeDoctor(id: Int): Boolean{
        val doctor = findById(id)
        return if (doctor != null){
            doctors.remove(doctor)
            true
        }else false
    }


}


