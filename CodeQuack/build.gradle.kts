plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.17.4"
}

group = "org.example"
version = "1.0-SNAPSHOT"

intellij {
    version.set("2024.3")
    type.set("IU")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.json:json:20240303")
}

tasks.test {
    useJUnitPlatform()
}

tasks {
    // Forsiramo Java 17 bez obzira koji JDK imas instaliran
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    patchPluginXml {
        sinceBuild.set("232")
        untilBuild.set("242.*")
    }

    runIde {
        autoReloadPlugins.set(true)
    }
}