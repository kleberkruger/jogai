plugins {
    base
}

group = "br.ufms"
version = "1.0.0"

repositories {
    google {
        content {
            includeGroupByRegex("androidx\\..*")
            includeGroupByRegex("com\\.android\\..*")
            includeGroupByRegex("com\\.google\\..*")
        }
    }
    mavenCentral()
}
