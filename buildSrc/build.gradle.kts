plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}
gradlePlugin {
    plugins {
        create("envPlugin") {
            id = "com.example.configuration"
            implementationClass = "GenerateConfiguration"
        }
    }
}

repositories {
    // The org.jetbrains.kotlin.jvm plugin requires a repository
    // where to download the Kotlin compiler dependencies from.
    mavenCentral()
}

dependencies {
    implementation("com.squareup:kotlinpoet:1.9.0")
}