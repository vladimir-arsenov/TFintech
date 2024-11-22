plugins {
    id("java")
    id("me.champeau.jmh") version "0.7.2"

}

group = "org.example"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    jmh ("org.openjdk.jmh:jmh-core:0.9")
    jmh ("org.openjdk.jmh:jmh-generator-annprocess:0.9")
    jmh ("org.openjdk.jmh:jmh-generator-bytecode:0.9")
    jmh("org.apache.kafka:kafka-clients:3.9.0")
    jmh("com.rabbitmq:amqp-client:5.23.0")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jmh {
    dependsOn(tasks.classes)
}