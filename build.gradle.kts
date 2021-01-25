plugins {
    kotlin("jvm") version "1.3.72"
    kotlin("kapt") version "1.3.72"
}

group = "me.mocha.spongeplugin"
version = "1.0-SNAPSHOT"
description = "korean game \"Seotda\" for sponge api"

repositories {
    mavenCentral()
    maven("https://repo.spongepowered.org/maven")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    val sponge = create("org.spongepowered:spongeapi:7.3.0")
    api(sponge)
    kapt(sponge)

    testImplementation("junit:junit:4.12")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}