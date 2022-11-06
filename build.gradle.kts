import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.5"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"

	// Spring Data JPA
	kotlin("plugin.jpa") version "1.6.21"
}

group = "com"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	// Kotlin
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	// Spring Web
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	// Spring JPA
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	// Postgres
	runtimeOnly("org.postgresql:postgresql")

	// Testing
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	// Swagger
	implementation("org.springdoc:springdoc-openapi-data-rest:1.6.12")
	implementation("org.springdoc:springdoc-openapi-ui:1.6.12")
	implementation("org.springdoc:springdoc-openapi-kotlin:1.6.12")
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
