plugins {
  id("com.gradleup.shadow") version "9.2.2"
  java
}

group = "me.m0dii"
version = "1.0.1"

java {
  sourceCompatibility = JavaVersion.VERSION_25
  targetCompatibility = JavaVersion.VERSION_25
}

java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

repositories {
  mavenCentral()
  maven(url = "https://hub.spigotmc.org/nexus/content/repositories/public/")
  maven(url = "https://maven.enginehub.org/repo/")
  maven{url = uri("https://jitpack.io")
  maven(url = "https://repo.aikar.co/content/groups/aikar/")
  maven(url = "https://repo.papermc.io/repository/maven-public/")

  flatDir {
    dirs("libs")
  }
}

dependencies {
  compileOnly("io.papermc.paper:paper-api:26.1.2.build.+")
  compileOnly("xyz.jpenilla:squaremap-api:1.1.6")

  compileOnly(libs("Residence6.0.1.8.jar"))
}

tasks.processResources {
  val props = mapOf("version" to project.version.toString())
  inputs.properties(props)
  filteringCharset = "UTF-8"
  filesMatching("plugin.yml") {
    expand(props)
  }
}

fun libs(name: String): FileCollection {
  return files("${rootDir.absolutePath}/libs/$name" + if (name.endsWith(".jar")) "" else ".jar")
}

tasks.build {
  dependsOn(tasks.named("shadowJar"))
}

