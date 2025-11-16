plugins {
  `java-library`
  id("io.papermc.paperweight.userdev") version "2.0.0-beta.18"
  id("xyz.jpenilla.run-paper") version "2.3.1" // Adds runServer and runMojangMappedServer tasks for testing
  id("xyz.jpenilla.resource-factory-paper-convention") version "1.3.1" // Generates plugin.yml based on the Gradle config
}

group = "com.yanisbft.commandhookpaper"
description = property("plugin.description").toString()
version = property("plugin.version").toString()

java {
  // Configure the java toolchain. This allows gradle to auto-provision JDK 21 on systems that only have JDK 11 installed for example.
  toolchain.languageVersion = JavaLanguageVersion.of(21)
}

dependencies {
  paperweight.paperDevBundle("1.21.10-R0.1-SNAPSHOT")

  testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
  testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.2")
  testImplementation("org.mockito:mockito-core:5.20.0")
}

tasks {
  compileJava {
    options.release = 21
  }
  javadoc {
    options.encoding = Charsets.UTF_8.name()
  }
  test {
    useJUnitPlatform()
  }
}

paperPluginYaml {
  main = property("paper.main-class").toString()
  apiVersion = property("paper.api-version").toString()
  authors.add("YanisBft")
  authors.add("_NewAge")
}
