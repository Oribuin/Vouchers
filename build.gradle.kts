import java.beans.JavaBean
import javax.swing.GroupLayout

plugins {
    `java-library`
    `maven-publish`
    id("com.gradleup.shadow") version "8.3.5"
}

group = "xyz.oribuin"
version = "1.0.5"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    
    disableAutoTargetJvm()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
    mavenLocal()

    maven("https://libraries.minecraft.net")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.rosewooddev.io/repository/public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.codemc.org/repository/maven-public/")
    maven("https://jitpack.io")
    maven("https://repo.mattstudios.me/artifactory/public/")
}

dependencies {
    api("dev.rosewood:rosegarden:1.5.1")
    api("dev.triumphteam:triumph-gui:3.1.11") {  // https://mf.mattstudios.me/triumph-gui/introduction
        exclude(group = "com.google.code.gson", module = "gson")
        exclude(group = "net.kyori", module = "*")
    }

    compileOnly("io.papermc.paper:paper-api:1.21.3-R0.1-SNAPSHOT")
    compileOnly("org.jetbrains:annotations:23.0.0")
    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("org.black_ixx:playerpoints:3.2.6")
    compileOnly("com.mojang:authlib:1.5.21")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7") {
        exclude(group = "org.bukkit", module = "*")
    }
}

tasks {
    this.compileJava {
        this.options.compilerArgs.add("-parameters")
        this.options.isFork = true
        this.options.encoding = "UTF-8"
    }
    
    this.shadowJar {
        this.archiveClassifier.set("")
        
        this.relocate("dev.rosewood.rosegarden", "${project.group}.vouchers.libs.rosegarden")
        this.relocate("dev.triumphtaem.gui", "${project.group}.vouchers.libs.triumphgui")
        
        this.exclude("dev/rosewood/rosegarden/lib/hikaricp/**/*.class")
        this.exclude("dev/rosewood/rosegarden/lib/slf4j/**/*.class")
    }
    
    this.processResources {
        this.expand("version" to project.version)
    }
    
    this.build {
        this.dependsOn(shadowJar)
    }
}
