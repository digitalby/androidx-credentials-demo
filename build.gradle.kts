// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ktlint)
}

// Apply ktlint to all subprojects that have Kotlin/XML source.
subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        version.set("1.4.1")
        android.set(true)
        outputToConsole.set(true)
        // Include XML resources alongside Kotlin sources.
        filter {
            include("**/*.kt")
            include("**/*.xml")
            exclude("**/build/**")
        }
    }
}
