plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.dv8tion:JDA:5.3.0")
    implementation ("com.google.code.gson:gson:2.12.1")
    implementation ("com.fasterxml.jackson.core:jackson-databind:2.18.3")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}