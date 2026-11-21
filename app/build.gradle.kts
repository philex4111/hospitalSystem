plugins {
    id("buildsrc.convention.kotlin-jvm")
    application
    kotlin("plugin.serialization") version "2.3.21"

}

dependencies {
    implementation(project(":utils"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
}

application {
    mainClass = "utumishiorg.example.app.AppKt"
}   