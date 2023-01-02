plugins {
    id("net.ltgt.errorprone") version "2.0.2" apply false
    id("org.sonarqube") version "3.5.0.2730"
}

sonarqube{
    properties{
        property("sonar.host.url", "https://sonarqube.hollowcube.net")
        property("sonar.sourceEncoding", "UTF-8")
        property("sonar.projectName", "common")
        property("sonar.projectKey", "common")
    }
}

subprojects {
    sonarqube {
        properties {
            property("sonar.sources", "src/main/")
        }
    }
}