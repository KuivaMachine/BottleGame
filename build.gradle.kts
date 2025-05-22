plugins {
    id("java")
    id("application")
}
group = "ru.zaostrovtsev"
version = "1.0"

application {
    mainClass.set("Game")
}
repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}