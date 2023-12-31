import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
  java
  application
  id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.ggjuanes.benevity-challenge"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
}

val vertxVersion = "4.4.4"
val junitJupiterVersion = "5.9.1"

val mainVerticleName = "com.ggjuanes.benevity_challenge.server.MainVerticle"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"

application {
  mainClass.set(launcherClassName)
}

dependencies {
  implementation("io.vertx:vertx-web-openapi:$vertxVersion")
  implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
  implementation("io.vertx:vertx-core")
  implementation("io.vertx:vertx-mongo-client:4.4.4")
  implementation("io.vertx:vertx-auth-common:4.4.4")
  implementation("io.vertx:vertx-auth-mongo:4.4.4")
  implementation("io.vertx:vertx-auth-jwt:4.4.4")
  implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
  implementation("ch.qos.logback:logback-classic:1.4.8")
  testImplementation("io.vertx:vertx-junit5")
  testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
  testImplementation("org.assertj:assertj-core:3.24.2")
  testImplementation("org.mockito:mockito-core:3.+")
  testImplementation("org.testcontainers:testcontainers:1.18.3")
  testImplementation("org.testcontainers:junit-jupiter:1.18.3")
  testImplementation("org.testcontainers:mongodb:1.18.3")
  testImplementation("org.apache.commons:commons-lang3:3.12.0")
}

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  manifest {
    attributes(mapOf("Main-Verticle" to mainVerticleName))
  }
  mergeServiceFiles()
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}

tasks.withType<JavaExec> {
  args = listOf("run", mainVerticleName, "--redeploy=$watchForChange", "--launcher-class=$launcherClassName", "--on-redeploy=$doOnChange")
}
