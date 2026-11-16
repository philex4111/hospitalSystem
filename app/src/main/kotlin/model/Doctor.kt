package utumishiorg.example.app.model

data class Doctor(
    val id: Int,
    val name: String,
    val speciality: String,
    val yearsExperience: Int = 0,
    val phone: String = "",
    val email: String = ""
)