package utumishiorg.example.app.model

import kotlinx.serialization.Serializable
import utumishiorg.example.app.model.enums.Gender
import utumishiorg.example.app.model.enums.BloodType

@Serializable
data class Patient(
    val id: Int,
    val name: String,
    val age: Int,
    val diagnosis: String,
    val gender: Gender = Gender.OTHER,
    val bloodType: BloodType? = null,
    val phone: String = "",
    val email: String = ""
){
    init {
        require(id > 0){"ID must be positive"}
        require(name.isNotBlank()){"Name can not be empty"}
        require(age in 0..150){"Age must be between 0 and 150"}
        require(diagnosis.isNotBlank()){"Diagnosis can not be empty"}
        if (email.isNotBlank()){
            require(email.contains("@")){"Invalid email format"}
        }
        if (phone.isNotBlank()){
            require(phone.length == 10){"enter phone number in this format: 0700000000"}
        }
    }
}