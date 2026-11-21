package utumishiorg.example.app.model

import utumishiorg.example.app.model.enums.Gender
import utumishiorg.example.app.model.enums.BloodType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import utumishiorg.example.app.service.PatientService

class PatientServiceTest {
    private lateinit var service: PatientService

    @BeforeEach
    fun setup(){
        service = PatientService()
    }
    @Test
    fun `addpatient succeeds with valid patient`(){
        val patient = Patient(1, "Liam pyne",30, "Flu")
        val result = service.addPatient(patient)
        assertTrue { result.isSuccess }
        assertEquals(1, service.getPatientCount())
    }
    @Test
    fun `addpatient fail with duplicate ID`(){
        val patient1 = Patient(1, "Liam Pyne", 30, "Flu")
        val patient2 = Patient(1, "john Doe",25, "Cold")
        service.addPatient(patient1)
        val result = service.addPatient(patient2)
        assertTrue(result.isFailure)
        assertEquals(1, service.getPatientCount())
    }

    @Test
    fun `findById return patient when exists`(){
        val patient = Patient(1, "Liam Pyne", 30, "Flu")
        service.addPatient(patient)
        val found = service.findById(1)
        assertNotNull(found)
        assertEquals("Liam Pyne", found?.name)
    }
    @Test
    fun `findById return null when not exists`(){
        val found = service.findById(999)
        assertNull(found)
    }

    @Test
    fun `findByName return matching patients`(){
        service.addPatient(Patient(1, "Liam Pyne", 30, "Flu"))
        service.addPatient(Patient(2, "John Doe", 25, "Cold"))
        service.addPatient(Patient(3, "Bob Johnson", 20, "flu"))

        val results = service.findByName("John")
        assertEquals(2, results.size) //"john Doe" and "Bob Johnson" contain "john"
    }

    @Test
    fun `updatePatient succeeds with valid update`(){
        service.addPatient(Patient(1, "Liam Pyne", 30, "Flu"))
        val updated = Patient(1, "Liam Pyne", 31, "Recovered")
        val result = service.updatePatient(1,updated)
        assertTrue (result.isSuccess)
        assertEquals("Liam Pyne", service.findById(1)?.name)
    }

    @Test
    fun `updatePatient fails when ID not found`(){
        val result = service.updatePatient(999, Patient(999, "X", 1, "Y"))
        assertTrue(result.isFailure)
    }

    @Test
    fun `removePatient return true when deleted`(){
        service.addPatient(Patient(1, "Liam Pyne", 30, "Flu"))
        val remove = service.removePatient(1)
        assertTrue(remove)
        assertEquals(0, service.getPatientCount())
    }
}