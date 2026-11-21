package utumishiorg.example.app.model

import kotlinx.serialization.Serializable

@Serializable
data class Doctor(
    val id: Int,
    val name: String,
    val specialty: String,
    val yearsExperience: Int = 0,
    val phone: String = "",
    val email: String = ""
){
    init {
        require(id >0){"Id must be positive"}
        require(name.isNotBlank()){"Name can not be empty"}
        require(specialty.isNotBlank()){"Speciality can not be blank"}
        require(yearsExperience in 0..100){"Experience must be between 0 to 100"}
        if (phone.isNotBlank()){
            require(phone.length == 10){"enter phone number in this format: 0700000000"}
        }
        if (email.isNotBlank()){
            require(email.contains("@")){"Invalid email format"}
        }
    }
}