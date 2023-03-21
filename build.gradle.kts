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

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    configurations["testDataImplementation"](project)
    configurations["testDataImplementation"]("org.springframework:spring-context")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(sourceSets["testData"].output)

    configurations["e2eSupportImplementation"](project)
    configurations["e2eSupportImplementation"](sourceSets["testData"].output)
    configurations["e2eSupportImplementation"]("org.springframework:spring-web")
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
    dependsOn("compileE2eSupportJava", "bootJar")
    archiveClassifier.set("e2e-support")
    mainClass.set("com.example.fixturepackaging.FixturePackagingApplicationKt")
    targetJavaVersion.set(JavaVersion.VERSION_17)

    classpath(sourceSets["e2eSupport"].output)
    classpath(sourceSets["testData"].output)
    classpath(tasks.bootJar.get().classpath)
}
