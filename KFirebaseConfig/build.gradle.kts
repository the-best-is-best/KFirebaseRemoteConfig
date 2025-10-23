
plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.maven.publish)
    id("maven-publish")
    id("signing")

}

extra["packageNameSpace"] = "io.github.kfirebase_config"
extra["groupId"] = "io.github.the-best-is-best"
extra["artifactId"] = "kfirebase-config"
extra["version"] = "2.1.0"
extra["packageName"] = "KFirebaseConfig"
extra["packageUrl"] = "https://github.com/the-best-is-best/KFirebaseRemoteConfig"
extra["packageDescription"] =
    "KFirebaseRemoteConfig: A Kotlin Multiplatform Firebase Remote Config library."
extra["system"] = "GITHUB"
extra["issueUrl"] = "https://github.com/the-best-is-best/KFirebaseRemoteConfig/issues"
extra["connectionGit"] = "https://github.com/the-best-is-best/KFirebaseRemoteConfig.git"

extra["developerName"] = "Michelle Raouf"
extra["developerNameId"] = "MichelleRaouf"
extra["developerEmail"] = "eng.michelle.raouf@gmail.com"


mavenPublishing {
    coordinates(
        extra["groupId"].toString(),
        extra["artifactId"].toString(),
        extra["version"].toString()
    )

    publishToMavenCentral(true)
    signAllPublications()

    pom {
        name.set(extra["packageName"].toString())
        description.set(extra["packageDescription"].toString())
        url.set(extra["packageUrl"].toString())
        licenses {
            license {
                name.set("Apache-2.0")
                url.set("https://opensource.org/licenses/Apache-2.0")
            }
        }
        issueManagement {
            system.set(extra["system"].toString())
            url.set(extra["issueUrl"].toString())
        }
        scm {
            connection.set(extra["connectionGit"].toString())
            url.set(extra["packageUrl"].toString())
        }
        developers {
            developer {
                id.set(extra["developerNameId"].toString())
                name.set(extra["developerName"].toString())
                email.set(extra["developerEmail"].toString())
            }
        }
    }

}


signing {
    useGpgCmd()
    sign(publishing.publications)
}

val packageName = extra["packageName"].toString()

kotlin {

// Target declarations - add or remove as needed below. These define
// which platforms this KMP module supports.
// See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
    androidLibrary {
        namespace = "io.github.tbib.kfirebaseconfig"
        compileSdk = 36
        minSdk = 23

        withHostTestBuilder {
        }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

// For iOS targets, this is also where you should
// configure native binary output. For more information, see:
// https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

// A step-by-step guide on how to include this library in an XCode
// project can be found here:
// https://developer.android.com/kotlin/multiplatform/migrate


    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
        macosX64(),
        macosArm64(),
        tvosX64(),
        tvosArm64(),
        tvosSimulatorArm64(),
//        watchosArm32(),
        watchosX64(),
        watchosArm64(),
        watchosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = packageName
            isStatic = true
        }
//        it.compilations.getByName("main") {
//            val defFileName = when (target.name) {
//                "iosX64" -> "iosX64.def"
//                "iosArm64" -> "iosArm64.def"
//                "iosSimulatorArm64" -> "iosSimulatorArm64.def"
//                "macosX64" -> "macosX64.def"
//                "macosArm64" -> "macosArm64.def"
//                "tvosX64" -> "tvosX64.def"
//                "tvosArm64" -> "tvosArm64.def"
//                "tvosSimulatorArm64" -> "tvosSimulatorArm64.def"
//                "watchosArm32" -> "watchosArm32.def"
//                "watchosX64" -> "watchosX64.def"
//                "watchosArm64" -> "watchosArm64.def"
//                "watchosSimulatorArm64" -> "watchosSimulatorArm64.def"
//
//
//                else -> throw IllegalStateException("Unsupported target: ${target.name}")
//            }
//
//            val defFile = project.file("src/interop/$defFileName")
//            if (defFile.exists()) {
//                cinterops.create("FirebaseConfig") {
//                    defFile(defFile)
//                    packageName = "io.github.native.kfirebase_config"
//                }
//            } else {
//                logger.warn("Def file not found for target ${target.name}: ${defFile.absolutePath}")
//            }
//        }

        it.compilations.getByName("main") {
            val firCrashlytics by cinterops.creating {
                defFile("/Users/michelleraouf/Desktop/kmm/KFirebaseRemoteConfig/KFirebaseConfig/src/interop/fir_config.def")
                packageName = "io.github.native.kfirebase_config"
            }

        }
    }


// Source set declarations.
// Declaring a target automatically creates a source set with the same name. By default, the
// Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
// common to share sources between related targets.
// See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                api(libs.kfirebase.core)
                implementation(libs.kotlinx.coroutines.core)
                api(libs.kotlinx.datetime)


                // Add KMP dependencies here
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {
                // Add Android-specific dependencies here. Note that this source set depends on
                // commonMain by default and will correctly pull the Android artifacts of any KMP
                // dependencies declared in commonMain.
                api(project.dependencies.platform(libs.firebase.bom)) // استخدم أحدث BOM
                implementation(libs.firebase.config.ktx)
            }
        }

        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.androidx.runner)
                implementation(libs.androidx.core)
                implementation(libs.androidx.junit)
            }
        }

        iosMain {
            dependencies {
                // Add iOS-specific dependencies here. This a source set created by Kotlin Gradle
                // Plugin (KGP) that each specific iOS target (e.g., iosX64) depends on as
                // part of KMP’s default source set hierarchy. Note that this source set depends
                // on common by default and will correctly pull the iOS artifacts of any
                // KMP dependencies declared in commonMain.
            }
        }
    }

}


abstract class GenerateDefFilesTask : DefaultTask() {

    @get:Input
    abstract val packageName: Property<String>

    @get:OutputDirectory
    abstract val interopDir: DirectoryProperty

    @TaskAction
    fun generate() {
        // Ensure the directory exists
        interopDir.get().asFile.mkdirs()

        // Constants
        val firebaseConfigHeaders = "FirebaseRemoteConfig.h"

//        // Map targets to their respective paths
//        val targetToPath = mapOf(
//            "iosX64" to "ios-arm64_x86_64-simulator",
//            "iosArm64" to "ios-arm64",
//            "iosSimulatorArm64" to "ios-arm64_x86_64-simulator",
//            "macosX64" to "macos-arm64_x86_64",
//            "macosArm64" to "macos-arm64_x86_64",
//            "tvosArm64" to "tvos-arm64",
//            "tvosX64" to "tvos-arm64_x86_64-simulator",
//            "tvosSimulatorArm64" to "tvos-arm64_x86_64-simulator",
//            "watchosSimulatorArm64" to "watchos-arm64_x86_64-simulator",
//            "watchosX64" to "watchos-arm64_arm64_32",
//            "watchosArm32" to "watchos-arm64_arm64_32",
//            "watchosArm64" to "watchos-arm64_arm64_32",
//        )

        // Helper function to generate header paths
        fun headerPath(): String {
            return interopDir.dir("libs/$firebaseConfigHeaders")
                .get().asFile.absolutePath
        }

        val defFile = File(interopDir.get().asFile, "fir_config.def")

        // Generate the content for the .def file
        val content = """
                language = Objective-C
                package = ${packageName.get()}
                headers = ${headerPath()}
            """.trimIndent()

        // Write content to the .def file
        defFile.writeText(content)
    }
}
// Register the task within the Gradle build
tasks.register<GenerateDefFilesTask>("generateDefFiles") {
    packageName.set("io.github.native.kfirebase_config")
    interopDir.set(project.layout.projectDirectory.dir("src/interop"))
}

tasks.named("build") {
    dependsOn(tasks.named("generateDefFiles"))
}
