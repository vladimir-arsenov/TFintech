plugins {
    java
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    jacoco
}

group = "org.example"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":execution-time-logger-starter"))
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.wiremock:wiremock-standalone:3.0.1")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    testImplementation("org.testcontainers:testcontainers:1.20.2")
    testImplementation("org.testcontainers:junit-jupiter:1.20.2")
    implementation("org.wiremock.integrations.testcontainers:wiremock-testcontainers-module:1.0-alpha-14")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")

}


tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)

    classDirectories.setFrom(
        fileTree( "${layout.buildDirectory.get().asFile}/classes/java/main") {
            exclude("**/model/**")
        }
    )
}

tasks.jacocoTestCoverageVerification {
    dependsOn("test")

    violationRules {
        rule {
            limit {
                minimum = "0.7".toBigDecimal()
            }
        }
    }
}