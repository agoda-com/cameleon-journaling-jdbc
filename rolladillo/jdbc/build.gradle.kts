plugins {
    kotlin("jvm")
}

group = "com.agoda.rolladillo"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.bundles.kotlinxEcosystem)
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(23)
}