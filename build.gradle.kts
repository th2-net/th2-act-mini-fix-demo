plugins {
    id("application")
    id("java")
    kotlin("jvm") version "1.8.22"
    id("com.exactpro.th2.gradle.component") version "0.0.4"
}

kotlin {
    jvmToolchain(11)
}

group = "com.exactpro.th2"
version = project.properties["release_version"].toString()

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
    resolutionStrategy.cacheDynamicVersionsFor(0, "seconds")
}

dependencies {
    implementation("com.exactpro.th2:common:5.10.0-dev")
    implementation("com.exactpro.th2:grpc-common:4.4.0-dev")
    implementation("com.exactpro.th2:grpc-hand:3.0.0-RM-84612-8595680156-664ca73-SNAPSHOT")
    implementation("com.exactpro.th2:hand:5.0.0-RM-84612-no-log4j-8545363969-62a26ef-SNAPSHOT")
    implementation("com.exactpro.th2:act-gui-core:3.0.0-RM-84612-8552406828-ac72781-SNAPSHOT")
    implementation("com.exactpro.th2:grpc-act-uiframework-win-demo:1.3.0-RM-84612-8544485219-ed352c1-SNAPSHOT")

    implementation("com.fasterxml.jackson.core:jackson-annotations")
    implementation("org.apache.commons:commons-lang3")
    implementation("org.apache.commons:commons-csv:1.10.0")
    implementation("com.google.guava:guava")

    implementation("org.slf4j:slf4j-log4j12:2.0.7")
}

application {
    applicationName = "service"
    mainClass.set("com.exactpro.th2.uiframework.ActMain")
}

repositories {
    mavenCentral()
    maven {
        name = "Sonatype_snapshots"
        url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    }
    maven {
        name = "Sonatype_releases"
        url = uri("https://s01.oss.sonatype.org/content/repositories/releases/")
    }
}