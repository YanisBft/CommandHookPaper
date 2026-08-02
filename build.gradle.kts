plugins {
  id("java")
  id("io.papermc.paperweight.userdev") version "2.0.0-beta.21"
  id("xyz.jpenilla.run-paper") version "2.3.1" // Adds runServer and runMojangMappedServer tasks for testing
  id("xyz.jpenilla.resource-factory-paper-convention") version "1.2.0" // Generates plugin.yml based on the Gradle config
}

group = "com.yanisbft.commandhookpaper"
description = property("plugin.description").toString()
version = property("plugin.version").toString()

java {
  toolchain.languageVersion = JavaLanguageVersion.of(25)
}

dependencies {
  paperweight.paperDevBundle("1.21.11-R0.1-SNAPSHOT")

  testRuntimeOnly("org.junit.platform:junit-platform-launcher:6.1.2")
  testImplementation("org.junit.jupiter:junit-jupiter:6.1.2")
  testImplementation("org.mockito:mockito-core:5.20.0")
}

tasks {
  compileJava {
    options.release = 25
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
