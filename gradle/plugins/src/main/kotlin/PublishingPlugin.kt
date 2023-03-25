import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.jvm.tasks.Jar
import org.gradle.plugins.signing.SigningExtension

class PublishingPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        pluginManager.apply("maven-publish")
        pluginManager.apply("signing")
        extensions.configure<PublishingExtension> {
            with(repositories) {
                maven { maven ->
                    maven.name = "github"
                    maven.setUrl("https://maven.pkg.github.com/usefulness/shimmer-android")
                    with(maven.credentials) {
                        username = "usefulness"
                        password = findConfig("GITHUB_TOKEN")
                    }
                }
                maven { maven ->
                    maven.name = "mavenCentral"
                    maven.setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                    maven.mavenContent { it.releasesOnly() }
                    with(maven.credentials) {
                        username = findConfig("OSSRH_USERNAME")
                        password = findConfig("OSSRH_PASSWORD")
                    }
                }
            }
        }

        pluginManager.withPlugin("signing") {
            with(extensions.extraProperties) {
                set("signing.keyId", findConfig("SIGNING_KEY_ID"))
                set("signing.password", findConfig("SIGNING_PASSWORD"))
                set("signing.secretKeyRingFile", findConfig("SIGNING_SECRET_KEY_RING_FILE"))
            }

            extensions.configure<SigningExtension>("signing") { signing ->
                if (findConfig("SIGNING_PASSWORD").isNotEmpty()) {
                    signing.sign(extensions.getByType(PublishingExtension::class.java).publications)
                }
            }
        }

        pluginManager.withPlugin("java") {
            extensions.configure<JavaPluginExtension> {
                withSourcesJar()
            }
            extensions.configure<PublishingExtension> {
                publications.configureEach { publication ->
                    (publication as? MavenPublication)?.pom { pom ->
                        pom.name.set("${project.group}:${project.name}")
                        pom.description.set("Screenshot Test for Android")
                        pom.url.set("https://github.com/usefulness/shimmer-android")
                        pom.licenses { licenses ->
                            licenses.license { license ->
                                license.name.set("MIT")
                                license.url.set("https://github.com/usefulness/shimmer-android/blob/master/LICENSE")
                            }
                        }
                        pom.developers { developers ->
                            developers.developer { developer ->
                                developer.id.set("mateuszkwiecinski")
                                developer.name.set("Mateusz Kwiecinski")
                                developer.email.set("36954793+mateuszkwiecinski@users.noreply.github.com")
                            }
                        }
                        pom.scm { scm ->
                            scm.connection.set("scm:git:github.com/usefulness/shimmer-android.git")
                            scm.developerConnection.set("scm:git:ssh://github.com/usefulness/shimmer-android.git")
                            scm.url.set("https://github.com/usefulness/shimmer-android/tree/master")
                        }
                    }
                }
            }
        }

        pluginManager.withPlugin("com.android.library") {
            tasks.register("androidSourcesJar", Jar::class.java) { jar ->
                jar.archiveClassifier.set("sources")
                val android = extensions.findByName("android") as com.android.build.gradle.BaseExtension
                jar.from(android.sourceSets.getByName("main").java.srcDirs)
                @Suppress("DEPRECATION")
                jar.from((android.sourceSets.getByName("main").kotlin as com.android.build.gradle.api.AndroidSourceDirectorySet).srcDirs)
            }
            extensions.configure<PublishingExtension> {
                afterEvaluate {
                    with(publications) {
                        register("release", MavenPublication::class.java) { publication ->
                            publication.from(components.getByName("release"))
                            publication.artifacts.artifact(tasks.getByName("androidSourcesJar"))
                            publication.pom { pom ->
                                pom.name.set("${project.group}:${project.name}")
                                pom.description.set("Screenshot Test for Android")
                                pom.url.set("https://github.com/usefulness/shimmer-android")
                                pom.licenses { licenses ->
                                    licenses.license { license ->
                                        license.name.set("MIT")
                                        license.url.set("https://github.com/usefulness/shimmer-android/blob/master/LICENSE")
                                    }
                                }
                                pom.developers { developers ->
                                    developers.developer { developer ->
                                        developer.id.set("mateuszkwiecinski")
                                        developer.name.set("Mateusz Kwiecinski")
                                        developer.email.set("36954793+mateuszkwiecinski@users.noreply.github.com")
                                    }
                                }
                                pom.scm { scm ->
                                    scm.connection.set("scm:git:github.com/usefulness/shimmer-android.git")
                                    scm.developerConnection.set("scm:git:ssh://github.com/usefulness/shimmer-android.git")
                                    scm.url.set("https://github.com/usefulness/shimmer-android/tree/master")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private inline fun <reified T> ExtensionContainer.configure(crossinline receiver: T.() -> Unit) {
        configure(T::class.java) { receiver(it) }
    }
}

private fun Project.findConfig(key: String): String {
    return findProperty(key)?.toString() ?: System.getenv(key) ?: ""
}
