package com.starbucks.shared.domain

import kotlinx.serialization.Serializable

@Serializable
data class Product (
    val id: String,
    val title: String,
    val description: String,
    val thumbnail: String,
    val category: MenuCategory,
    val subCategory: SubCategory?  = null,
    val sizes: List<Size>? = null,
    val isAvailable: Boolean,
    val isNew: Boolean,
    val isDiscounted: Boolean,
    val isPopular: Boolean,
)

@Serializable
data class SubCategory(
    val id: String,
    val title: String,
    val imageUrl: String
)

@Serializable
data class Size(
    val name: String,
    val price: Double,
    val imageUrl: String
)

// หมวดหมู่หลัก
@Serializable
enum class MenuCategory {
    BEVERAGE,
    FOOD
}

// Subcategory สำหรับเครื่องดื่ม
enum class BeverageSubCategory (
    val id: String,
    val title: String,
    val imageUrl: String
){
    HOT_COFFEE(
        id = "hot_coffee",
        title = "Hot Coffee",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/BeverageImage%2FHOT_COFFEE.png?alt=media&token=e16d9b97-7e21-4162-9bc6-7768a9efd77c"
    ),
    HOT_TEA(
        id = "hot_tea",
        title = "Hot Tea",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/BeverageImage%2FHOT_TEA.png?alt=media&token=866fb761-1b21-47f7-9e43-f353ffd0fba5"
    ),
    OTHER_HOT_BEVERAGES(
        id = "other_hot_beverages",
        title = "Other Hot Beverages",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/BeverageImage%2FOTHER_HOT_BEVERAGES.png?alt=media&token=7066fb9d-9f16-4809-9216-c37a4fd6420b"
    ),
    COLD_COFFEE(
        id = "cold_coffee",
        title = "Cold Coffee",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/BeverageImage%2FCOLD_COFFEE.png?alt=media&token=cd355d19-acb6-4f49-a14a-1c6fe404c822"
    ),
    ICED_TEA(
        id = "iced_tea",
        title = "Iced Tea",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/BeverageImage%2FICED_TEA.png?alt=media&token=225f4ec2-aacb-4527-8bbb-9ec3a5fd044b"
    ),
    OTHER_COLD_BEVERAGES(
        id = "other_cold_beverages",
        title = "Other Cold Beverages",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/BeverageImage%2FOTHER_COLD_BEVERAGES.png?alt=media&token=e9c801dd-162f-411f-9a81-b390988d9ac0"
    ),
    FRAPPUCCINO(
        id = "frappuccino",
        title = "Frappuccino® Blended Beverages",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/BeverageImage%2FFRAPPUCCINO.png?alt=media&token=4fafeddb-8a81-4b12-b20f-6bbeabcac288"
    ),
    STARBUCKS_RESERVE(
        id = "starbucks_reserve",
        title = "Starbucks Reserve® Beverages",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/BeverageImage%2FSTARBUCKS_RESERVE.jpg?alt=media&token=bb755f5e-14ad-487a-ab16-8e8b7351cc5a"
    ),
    EXCLUSIVE_DRINKS(
        id = "exclusive_drinks",
        title = "Exclusive Menu",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/BeverageImage%2FEXCLUSIVE.jpg?alt=media&token=9ab1b1bd-db5b-4153-b06b-09f3dc98108b"
    ),
    BOTTLED_DRINKS(
        id = "bottled_drinks",
        title = "Bottled Drinks",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/BeverageImage%2FBOTTLED_DRINKS.png?alt=media&token=1c6c3e16-16b8-4ddc-93f2-75c96e4d769f"
    )
}

// Subcategory สำหรับอาหาร
enum class FoodSubCategory (
    val id: String,
    val title: String,
    val imageUrl: String
){
    BAKERY(
        id = "bakery",
        title = "Bakery",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/FoodImage%2FBAKERY.png?alt=media&token=7624f661-04c0-4c07-9dd5-5f3d621571b6"
    ),
    CAKE(
        id = "cake",
        title = "Cake",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/FoodImage%2FCAKE.png?alt=media&token=b4b0e7f7-b483-49fe-874f-1a2fbe778f16"
    ),
    SANDWICH_BISTRO(
        id = "sandwich_bistro",
        title = "Sandwich & BISTRO",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/FoodImage%2FSANDWICH_BISTRO.jpg?alt=media&token=bb540d25-7d9a-4c05-97ec-4d105b833557"

    ),
    SOUP_PASTA(
        id = "soup_pasta",
        title = "Soup & Pasta",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/FoodImage%2FSOUP_PASTA.png?alt=media&token=f9637ce4-a6e1-4f14-838d-c5b2718d2f1e"
    ),
    SALAD_YOGURT(
        id = "salad_yogurt",
        title = "Salad & Yogurt",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/FoodImage%2FSALAD_YOGURT.jpg?alt=media&token=b9bc669a-c506-4ff9-ad73-c7ab214814f5"
    ),
    PACKAGED_FOOD(
        id = "packaged_food",
        title = "Packaged Food",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/FoodImage%2FPACKAGED_FOOD.jpg?alt=media&token=3fe33088-6ca1-468e-bf0b-85959e0da1c1"
    ),
    DESSERT_ICE_CREAM(
        id = "dessert_ice_cream",
        title = "Dessert & Ice Cream",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/FoodImage%2FDESSERT_ICE_CREAM.png?alt=media&token=711c872e-eadc-46dc-9293-35cc7863a70a"
    ),
    FRESHLY_BAKED(
        id = "freshly_baked",
        title = "Freshly Baked",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/FoodImage%2FFRESHLY_BAKED.jpg?alt=media&token=659fac8d-4526-454a-80e7-89190329f1d7"
    ),
    EXCLUSIVE_FOOD(
        id = "exclusive_food",
        title = "Exclusive Menu",
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/FoodImage%2FEXCLUSIVE_FOOD.jpg?alt=media&token=08501637-814c-4960-bae8-8b71085178cd"
    )
}

// Extension functions แปลง enum เป็น SubCategory
fun BeverageSubCategory.toSubCategory() = SubCategory(
    id = this.id,
    title = this.title,
    imageUrl = this.imageUrl
)

fun FoodSubCategory.toSubCategory() = SubCategory(
    id = this.id,
    title = this.title,
    imageUrl = this.imageUrl
)