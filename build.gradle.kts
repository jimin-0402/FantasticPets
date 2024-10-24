plugins {
    id("java")
    id("io.github.goooler.shadow") version "8.1.8"
}

group = "kr.jimin.fantastic.pets"
version = "1.0-SNAPSHOT"

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.oraxen.com/releases")
    maven("https://jitpack.io")
    mavenCentral()
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")

    implementation("net.kyori:adventure-platform-bukkit:4.3.4")
    implementation("dev.jorel:commandapi-bukkit-shade:9.5.2")

    compileOnly("net.luckperms:api:5.4")
    compileOnly("io.th0rgal:oraxen:1.182.0")
    compileOnly("com.github.LoneDev6:API-ItemsAdder:3.6.1")
    compileOnly("com.arcaniax:HeadDatabase-API:1.3.2")
    compileOnly(files("C:/developement/minecraft-java/FantasticPets/FantasticPets/libs/MCPets.jar"))
}

tasks {
    processResources {
        val props = mapOf("version" to version)
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    shadowJar {
        archiveFileName.set("${rootProject.name}-$version.jar")
        destinationDirectory.set(file("C:/Users/aa990/OneDrive/바탕 화면/test server/server/plugins"))
    }

    build.get().dependsOn(shadowJar)



    test { useJUnitPlatform() }
}