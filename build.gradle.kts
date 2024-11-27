plugins {
    java
    id("io.github.goooler.shadow") version "8.1.8"
}

group = "kr.jimin.fantastic.pets"
version = "1.0.6"

val paperVersion = "1.20.1-R0.1-SNAPSHOT"
val commandApiVersion = "9.6.1"
val platformVersion = "4.3.4"
val bStatsVersion = "3.0.2"

val luckPermsVersion = "5.4"
val oraxenVersion = "1.182.0"
val nexoVersion = "0.1.0-dev.0"
val itemsAdderVersion = "3.6.1"
val headDatabaseVersion = "1.3.2"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") // paper
    maven("https://repo.oraxen.com/releases") // Oraxen
    maven("https://repo.nexomc.com/snapshots/") // Nexo
    maven("https://repo.codemc.org/repository/maven-public/") // CommandAPI
    maven("https://jitpack.io") // ItemsAdder
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:$paperVersion")

    implementation("dev.jorel:commandapi-bukkit-shade:$commandApiVersion") // CommandAPI
    implementation("org.bstats:bstats-bukkit:$bStatsVersion") // bStats
    implementation("net.kyori:adventure-platform-bukkit:$platformVersion")

    compileOnly("net.luckperms:api:$luckPermsVersion") // LuckPerms
    compileOnly("io.th0rgal:oraxen:$oraxenVersion") // Oraxen
    compileOnly("com.nexomc:nexo:$nexoVersion") // Nexo
    compileOnly("com.github.LoneDev6:API-ItemsAdder:$itemsAdderVersion") // ItemsAdder
    compileOnly("com.arcaniax:HeadDatabase-API:$headDatabaseVersion") // HeadDatabase
    compileOnly(files("C:/developement/minecraft-java/FantasticPets/FantasticPets/libs/MCPets.jar")) // McPets
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
        relocate("org.bstats", "kr.jimin.fantastic.pets.shadow.bstats")
        relocate("dev.jorel.commandapi", "kr.jimin.fantastic.pets.shadow.commandapi")

        mergeServiceFiles()
    }

    build.get().dependsOn(shadowJar)

    test { useJUnitPlatform() }
}