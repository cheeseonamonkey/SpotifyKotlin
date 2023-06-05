plugins {
    kotlin("js") version "1.7.21"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.5.21"

}

group = "me.alexander"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
    maven("https://dl.bintray.com/robert-cronin/fortytwoapps")
}

dependencies {

    //kotlinx
    implementation("org.jetbrains.kotlinx:kotlinx-html:0.7.2")

    //async:
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.6.4")

    //libs:

    implementation(npm("js-cookie", "3.0.1"))
    implementation(npm("md5", "2.3.0"))
    implementation(npm("swiper", "9.0.5"))

    //csv
    implementation("com.github.doyaaaaaken:kotlin-csv-js:1.8.0")

    //regression math
    //implementation("org.apache.commons:commons-math3:3.6.1")

}

kotlin {
    js(IR) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
    }
}