package com.starbucks.home.domain

import com.starbucks.shared.Resources
import org.jetbrains.compose.resources.DrawableResource

enum class DrawerItem (
    val titleKey: String,
    val icon: DrawableResource
){
    Profile(
        titleKey = "profile",
        icon = Resources.Icon.Person
    ),
    Blog(
        titleKey = "blog",
        icon = Resources.Icon.Book
    ),
    Locations(
        titleKey = "location",
        icon = Resources.Icon.MapPin
    ),
    Contact(
        titleKey = "contact_us",
        icon = Resources.Icon.Edit
    ),
    SignOut(
        titleKey = "sign_out",
        icon = Resources.Icon.SignOut
    ),
    Admin(
        titleKey = "admin_panel",
        icon = Resources.Icon.Unlock
    )
}