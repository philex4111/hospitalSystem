package utumishiorg.example.app.ui

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Scanner

private val scanner = Scanner(System.`in`)

fun readInt(prompt: String): Int {
    while (true) {
       print(prompt)
        val input = scanner.nextLine()
        val parsed = input.toIntOrNull()
        if (parsed != null)
        return parsed
        println("Invalid input. Please enter a number.")
    }
}

fun readIntInRange(prompt: String, min: Int, max: Int): Int {
    while (true) {
        val value = readInt(prompt)
        if (value in min..max) return value
        println("please enter a number between $min and $max.")
    }
}

fun readString(prompt: String): String {
    print(prompt)
    while (true) {
        val input = scanner.nextLine().trim()
        if (input.isNotEmpty()) return input
        println("Input cannot be empty. Try again.")
    }
}

fun readNonEmptyString(prompt: String, validation:(String) -> Boolean, errorMap: String): String {
    while (true) {
        val input = readString(prompt)
        if (validation(input)) return input
        println(errorMap)
    }
}

fun readDateTime(prompt: String): LocalDateTime? {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    while (true){
        print(prompt)
        val input = scanner.nextLine().trim()
        return try {
            LocalDateTime.parse(input, formatter)
        } catch (e: Exception) {
            println("Invalid format. Use yyyy-MM-dd HH:mm")
            null
        }
    }
}


fun readDate(prompt: String): LocalDateTime? {
    while (true){
        print(prompt)
        val input = scanner.nextLine().trim()
        return try {
            LocalDateTime.parse(input + "T00:00")
        } catch (e: Exception) {
            println("Invalid format. Use yyyy-MM-dd")
            null
        }
    }
    }

fun readOptionalString(prompt: String): String {
    print(prompt)
    return scanner.nextLine().trim()
}
