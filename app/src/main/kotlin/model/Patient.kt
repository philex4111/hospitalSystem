package utumishiorg.example.app.model

import utumishiorg.example.app.model.enums.Gender
import utumishiorg.example.app.model.enums.BloodType

//data class = auto-generate toString(), equals(), hashCode(), copy()
data class Patient(
    val id: Int,
    val name: String,
    val age: Int,
    val diagnosis: String,
    val gender: Gender =Gender.OTHER,
    val bloodTypy: BloodType? = null,
    val phone: String ="",
    val email: String =""
)