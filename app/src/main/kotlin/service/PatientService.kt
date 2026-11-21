package utumishiorg.example.app.service

import kotlinx.serialization.Serializable
import utumishiorg.example.app.model.Patient
import utumishiorg.example.app.model.enums.Gender
@Serializable
class PatientService{
    //private storage only this class can modify it
    private val patients = mutableListOf<Patient>()

    //--CREATE--
    fun addPatient(patient: Patient): Result<Patient>{
        //check if duplicate id
        if(patients.any(){it.id == patient.id}){
            return Result.failure(IllegalArgumentException("Patient with ID ${patient.id} already exists"))
        }
        patients.add(patient)
        return Result.success(patient)
    }

    //--READ--
    fun getAllPatients():
            List<Patient> = patients.toList()
    //Return copy

    fun getPatientCount(): Int =
        patients.size

    fun findById(id: Int): Patient? = patients.find {it.id ==id
    }

    fun findByName(name: String): List<Patient> =
        patients.filter{it.name.contains(name, ignoreCase = true )}

    fun findByDiagnosis(diagnosis: String): List<Patient> =patients.filter { it.diagnosis.contains(diagnosis, ignoreCase = true) }

    fun findByGender(gender: Gender): List<Patient> =
        patients.filter { it.gender == gender }

    fun findByAgeRange(minAge: Int, maxAge: Int): List<Patient> =
        patients.filter{it.age in minAge..maxAge}

    //--UPDATE
    fun updatePatient(id: Int, update: Patient): Result<Patient>{
        val index = patients.indexOfFirst { it.id == id }
        if(index < 0){
            return Result.failure(IllegalArgumentException("patient with ID ${update.id} already exists"))
        }
        patients[index] = update
        return Result.success(update)
    }
    //--DELETE--
    fun removePatient(id: Int): Boolean{
        val patient = findById(id)
        return if (patient!= null){
            patients.remove(patient)
            true
        }else false
    }
}