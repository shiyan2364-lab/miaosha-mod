plugins {
    id("net.fabricmc.fabric-loom") version "1.17.21"
    `maven-publish`
}
repositories { mavenCentral() }
dependencies {
    val mc: String by project
    val ldr: String by project
    val api: String by project
    minecraft("com.mojang:minecraft:$mc")
    modImplementation("net.fabricmc:fabric-loader:$ldr")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$api")
}
processResources {
    inputs.property("version", project.version)
    filesMatching("fabric.mod.json") { expand("version" to project.version) }
}
tasks.withType<JavaCompile>().configureEach { options.release.set(16) }
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
