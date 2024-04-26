pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
//            setUrl ("https://jcenter.bintray.com")
            setUrl ("https://jitpack.io")

        }
    }
}

rootProject.name = "TestApp"
include(":app")
