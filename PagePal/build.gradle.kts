import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("org.json:json:20231013")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    //implementation("org.mongodb:mongodb-driver-kotlin-coroutine:4.11.0")
    implementation(compose.desktop.currentOs)
    testImplementation(kotlin("test"))
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Exe)
            packageName = "team-102-5"
            packageVersion = "1.0.0"

            appResourcesRootDir.set(project.layout.projectDirectory.dir("resources"))
        }
    }
}