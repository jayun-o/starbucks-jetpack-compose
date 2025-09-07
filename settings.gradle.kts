rootProject.name = "Starbucks"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}
include(":data")
include(":di")
include(":di")
include(":feature:admin_panel")
include(":feature:admin_panel:manage_product")

include(":feature:map")
include(":feature:home")
include(":feature:profile")
include(":feature:auth")
include(":navigation")
include(":composeApp")
include(":shared")
