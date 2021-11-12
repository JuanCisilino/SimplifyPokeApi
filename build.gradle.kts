import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.5.6"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.5.31"
	kotlin("plugin.spring") version "1.5.31"
}

group = "com.simplifly"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc:2.5.6")
	implementation("org.springframework.boot:spring-boot-starter-validation:2.5.6")
	implementation("org.springframework.boot:spring-boot-starter-webflux:2.5.6")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.0")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.1.5")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.5.2-native-mt")
	implementation("org.springframework.boot:spring-boot-devtools:2.5.6")
	implementation("com.h2database:h2:1.4.200")
	runtimeOnly("io.r2dbc:r2dbc-h2:0.8.4.RELEASE")
	testImplementation("org.springframework.boot:spring-boot-starter-test:2.5.6")
	testImplementation("io.projectreactor:reactor-test:3.4.12")

	implementation("com.google.code.gson:gson:2.8.9")
	implementation("org.springframework.boot:spring-boot-starter-hateoas:2.5.6")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	runtimeOnly("org.postgresql:postgresql:42.3.1")
	implementation("org.jetbrains.exposed:exposed-core:0.36.1")
	implementation("org.jetbrains.exposed:exposed-dao:0.36.1")
	implementation("org.jetbrains.exposed:exposed-jdbc:0.36.1")
	testImplementation("org.springframework.boot:spring-boot-starter-test:2.5.6") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
