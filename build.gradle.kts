import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

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
        kotlin {
            srcDir("$projectDir/src/testData/kotlin")
        }
    }
    create("e2eSupport") {
        kotlin {
            srcDir("$projectDir/src/e2eSupport/kotlin")
        }
    }
}

fun DependencyHandler.testDataImplementation(dependencyNotation: Any): Dependency? =
    add("testDataImplementation", dependencyNotation)

fun DependencyHandler.e2eSupportImplementation(dependencyNotation: Any): Dependency? =
    add("e2eSupportImplementation", dependencyNotation)

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    testDataImplementation(project)
    testDataImplementation("org.springframework:spring-context")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(sourceSets["testData"].output)

    e2eSupportImplementation(project)
    e2eSupportImplementation(sourceSets["testData"].output)
    e2eSupportImplementation("org.springframework:spring-web")
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

tasks.create("bootE2ESupportJar", BootJar::class.java) {
    archiveClassifier.set("e2e-support")
    mainClass.set("com.example.fixturepackaging.FixturePackagingApplicationKt")
    targetJavaVersion.set(JavaVersion.VERSION_17)

    classpath(sourceSets["e2eSupport"].output)
    classpath(sourceSets["testData"].output)
    classpath(tasks.bootJar.get().classpath)
}
