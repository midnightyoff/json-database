plugins {
    id("java")
}

group = "com.projects"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.jcommander:jcommander:3.0")
    implementation ("com.google.code.gson:gson:2.10.1")
}

tasks.test {
    useJUnitPlatform()
}