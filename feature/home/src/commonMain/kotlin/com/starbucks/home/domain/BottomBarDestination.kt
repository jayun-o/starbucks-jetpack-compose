package com.starbucks.home.domain

import com.starbucks.shared.Resources
import com.starbucks.shared.navigation.Screen
import org.jetbrains.compose.resources.DrawableResource

enum class BottomBarDestination(
    val icon: DrawableResource,
    val titleKey: String,
    val screen: Screen
) {
    ProductsOverview(
        icon = Resources.Icon.Home,
        titleKey = "bottom_bar_home",
        screen = Screen.ProductsOverview
    ),
    Cart(
        icon = Resources.Icon.ShoppingCart,
        titleKey = "bottom_bar_order",
        screen = Screen.Cart
    ),
    Categories(
        icon = Resources.Icon.Categories,
        titleKey = "bottom_bar_categories",
        screen = Screen.Categories
    )
}
