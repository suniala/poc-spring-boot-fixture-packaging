import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.4"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.spring") version "1.7.22"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

sourceSets {
    create("testData") {
        kotlin { srcDir("$projectDir/src/testData/kotlin") }
    }
    create("e2eSupport") {
        kotlin { srcDir("$projectDir/src/e2eSupport/kotlin") }
    }
}

dependencies {
    api("org.springframework.boot:spring-boot-starter-web")
    api("com.fasterxml.jackson.module:jackson-module-kotlin")
    api("org.jetbrains.kotlin:kotlin-reflect")

    // Configuration with name 'main' not found.
    // configurations["testDataImplementation"](project.configurations["main"])
    // Works in gradle but prod sources not imported to testData
    //    configurations["testDataApi"](project.configurations["api"])
    configurations["testDataImplementation"](project)

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(sourceSets["testData"].output)

    // Cannot convert the provided notation to an object of type Dependency: source set 'test data'.
    //    configurations["e2eSupportImplementation"](project.sourceSets["testData"])
    // Project with path 'fixture-packaging' could not be found in root project 'fixture-packaging'.
    //    configurations["e2eSupportImplementation"](project(path="fixture-packaging", configuration="testDataImplementation"))
    configurations["e2eSupportImplementation"](project)
    configurations["e2eSupportImplementation"](sourceSets["testData"].output)
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
