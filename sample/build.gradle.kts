plugins {
    kotlin("multiplatform")
    id("de.infix.testBalloon")
}

kotlin {
    jvm()

    sourceSets {
        commonTest.dependencies {
            implementation("org.jetbrains.kotlin:kotlin-test")
            implementation("de.infix.testBalloon:testBalloon-framework-core:1.0.1-K2.4.0")
        }
    }
}
