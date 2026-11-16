package utumishiorg.example.app.ui

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun readInt(prompt: String): Int{
    while (true){
        print(prompt)
        val input = readln()
        return input.toIntOrNull() ?: run  {
            println("Invalid number. Try again.")
            -1
        }
    }
}

fun readString(prompt: String): String{
    print(prompt)
    return readln().trim()
}

fun readDateTime(prompt: String): LocalDateTime?{
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    print(prompt)
    val input = readln().trim()
    return try{
        LocalDateTime.parse(input, formatter)
    } catch (e: Exception) {
        println("Invalid format. use yyyy-mm-dd HH:mm")
        null
    }
}

fun readDate(prompt: String): LocalDateTime?{
    print(prompt)
    val input = readln().trim()
    return try {
        LocalDateTime.parse(input +"T00:00")
    }catch (e: Exception){
        println("Invalid format. Use yyyy-mm-dd")
        null
    }
}



