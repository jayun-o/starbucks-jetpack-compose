package com.starbucks.shared.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Serializable
data class Product(
    val id: String,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val title: String,
    val description: String,
    val thumbnail: String,
    val category: ProductCategory,
    val subCategory: SubCategory,
    val price: Double? = null,
    val sizes: List<Size>? = null,
    val isAvailable: Boolean = false,
    val isNew: Boolean = false,
    val isDiscounted: Boolean = false,
    val discounted: Int?,
    val isPopular: Boolean = false,

    //CUSTOMIZATION
    val isCoffeeShot: Boolean = false,
    val isMilk: Boolean = false,
    val isSweetness: Boolean = false,
    val isFlavorAndSyrup: Boolean = false,
    val isCondiment: Boolean = false,
    val isToppings: Boolean = false,
    val isCutlery: Boolean = false,
    val isWarmUp: Boolean = false
)

@Serializable
data class SubCategory(
    val id: String,
    val title: String
)

@Serializable
data class Category(
    val id: String = "",
    val title: String = "",
)

@Serializable
data class Size(
    val name: String,
    val price: Double,
)

@Serializable
enum class ProductCategory (
    val id: String,
    val title: String,
){
    @SerialName("beverage")
    BEVERAGE("beverage", "Beverage"),
    @SerialName("food")
    FOOD("food", "Food");
}

enum class BeverageSubCategory (
    val id: String,
    val title: String,
    val imageUrl: String
){
    HOT_COFFEE(
        "hot_coffee",
        "Hot Coffee",
        "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/BeverageImage%2FHOT_COFFEE.png?alt=media&token=e16d9b97-7e21-4162-9bc6-7768a9efd77c"
    ),
    HOT_TEA(
        "hot_tea",
        "Hot Tea",
        "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/BeverageImage%2FHOT_TEA.png?alt=media&token=866fb761-1b21-47f7-9e43-f353ffd0fba5"
    ),
    OTHER_HOT_BEVERAGES(
        "other_hot_beverages",
        "Other Hot Beverages",
        "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/BeverageImage%2FOTHER_HOT_BEVERAGES.png?alt=media&token=7066fb9d-9f16-4809-9216-c37a4fd6420b"
    ),
    COLD_COFFEE(
        "cold_coffee",
        "Cold Coffee",
        "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/BeverageImage%2FCOLD_COFFEE.png?alt=media&token=cd355d19-acb6-4f49-a14a-1c6fe404c822"
    ),
    ICED_TEA(
        "iced_tea",
        "Iced Tea",
        "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/BeverageImage%2FICED_TEA.png?alt=media&token=225f4ec2-aacb-4527-8bbb-9ec3a5fd044b"
    ),
    OTHER_COLD_BEVERAGES(
        "other_cold_beverages",
        "Other Cold Beverages",
        "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/BeverageImage%2FOTHER_COLD_BEVERAGES.png?alt=media&token=e9c801dd-162f-411f-9a81-b390988d9ac0"
    ),
    FRAPPUCCINO(
        "frappuccino",
        "Frappuccino® Blended Beverages",
        "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/BeverageImage%2FFRAPPUCCINO.png?alt=media&token=4fafeddb-8a81-4b12-b20f-6bbeabcac288"
    ),
    STARBUCKS_RESERVE(
        "starbucks_reserve",
        "Starbucks Reserve® Beverages",
        "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/BeverageImage%2FSTARBUCKS_RESERVE.jpg?alt=media&token=bb755f5e-14ad-487a-ab16-8e8b7351cc5a"
    ),
    EXCLUSIVE_DRINKS(
        "exclusive_drinks",
        "Exclusive Menu",
        "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/BeverageImage%2FEXCLUSIVE.jpg?alt=media&token=9ab1b1bd-db5b-4153-b06b-09f3dc98108b"
    ),
    BOTTLED_DRINKS(
        "bottled_drinks",
        "Bottled Drinks",
        "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/BeverageImage%2FBOTTLED_DRINKS.png?alt=media&token=1c6c3e16-16b8-4ddc-93f2-75c96e4d769f"
    )
}

// Subcategory สำหรับอาหาร
enum class FoodSubCategory (
    val id: String,
    val title: String,
    val imageUrl: String
){
    BAKERY(
        "bakery",
        "Bakery",
        "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/FoodImage%2FBAKERY.png?alt=media&token=7624f661-04c0-4c07-9dd5-5f3d621571b6"
    ),
    CAKE(
        "cake",
        "Cake",
        "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/FoodImage%2FCAKE.png?alt=media&token=b4b0e7f7-b483-49fe-874f-1a2fbe778f16"
    ),
    SANDWICH_BISTRO(
        "sandwich_bistro",
        "Sandwich & Bistro",
        "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/FoodImage%2FSANDWICH_BISTRO.jpg?alt=media&token=bb540d25-7d9a-4c05-97ec-4d105b833557"
    ),
    SOUP_PASTA(
        "soup_pasta",
        "Soup & Pasta",
        "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/FoodImage%2FSOUP_PASTA.png?alt=media&token=f9637ce4-a6e1-4f14-838d-c5b2718d2f1e"
    ),
    SALAD_YOGURT(
        "salad_yogurt",
        "Salad & Yogurt",
        "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/FoodImage%2FSALAD_YOGURT.jpg?alt=media&token=b9bc669a-c506-4ff9-ad73-c7ab214814f5"
    ),
    PACKAGED_FOOD(
        "packaged_food",
        "Packaged Food",
        "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/FoodImage%2FPACKAGED_FOOD.jpg?alt=media&token=3fe33088-6ca1-468e-bf0b-85959e0da1c1"
    ),
    DESSERT_ICE_CREAM(
        "dessert_ice_cream",
        "Dessert & Ice Cream",
        "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/FoodImage%2FDESSERT_ICE_CREAM.png?alt=media&token=711c872e-eadc-46dc-9293-35cc7863a70a"
    ),
    FRESHLY_BAKED(
        "freshly_baked",
        "Freshly Baked",
        "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/FoodImage%2FFRESHLY_BAKED.jpg?alt=media&token=659fac8d-4526-454a-80e7-89190329f1d7"
    ),
    EXCLUSIVE_FOOD(
        "exclusive_food",
        "Exclusive Menu",
        "https://firebasestorage.googleapis.com/v0/b/starbucks-465316.firebasestorage.app/o/FoodImage%2FEXCLUSIVE_FOOD.jpg?alt=media&token=08501637-814c-4960-bae8-8b71085178cd"
    )
}

// --- Extension functions ---
fun BeverageSubCategory.toSubCategory() = SubCategory(
    id = this.id,
    title = this.title
)

fun FoodSubCategory.toSubCategory() = SubCategory(
    id = this.id,
    title = this.title
)

fun ProductCategory.displayName(): String = this.title

// ดึง SubCategory ทั้งหมดจาก Category
fun getSubCategoriesFor(category: ProductCategory): List<SubCategory> {
    return when (category) {
        ProductCategory.BEVERAGE -> BeverageSubCategory.entries.map { it.toSubCategory() }
        ProductCategory.FOOD -> FoodSubCategory.entries.map { it.toSubCategory() }
    }
}

// ดึง imageUrl ของ SubCategory ตาม category + id
fun getSubCategoryImage(subCategoryId: String, category: ProductCategory): String? {
    return when (category) {
        ProductCategory.BEVERAGE -> BeverageSubCategory.entries.find { it.id == subCategoryId }?.imageUrl
        ProductCategory.FOOD -> FoodSubCategory.entries.find { it.id == subCategoryId }?.imageUrl
    }
}
