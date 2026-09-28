plugins {
    id("net.fabricmc.fabric-loom") version "0.12-SNAPSHOT"
    `maven-publish`
}

repositories {
    mavenCentral()
}

dependencies {
    val minecraft_version: String by project
    val loader_version: String by project
    val fabric_api_version: String by project

    minecraft("com.mojang:minecraft:$minecraft_version")
    minecraftName("client")
    modImplementation("net.fabricmc:fabric-loader:$loader_version")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabric_api_version")
}

processResources {
    val version = project.version
    inputs.property("version", version)
    filesMatching("fabric.mod.json") {
        expand("version" to version)
    }
}

tasks.withType<JavaCompile>().configureEach {
    it.options.release.set(16)
}

java {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
    withSourcesJar()
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${project.archivesBaseName()}" }
    }
}
