package com.starbucks.details

import ContentWithMessageBar
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.starbucks.details.component.BottomSheet
import com.starbucks.details.component.ResetButton
import com.starbucks.details.component.SingleSelectBottomSheet
import com.starbucks.details.component.SizeSelector
import com.starbucks.details.component.Stepper
import com.starbucks.shared.Alpha
import com.starbucks.shared.BorderIdle
import com.starbucks.shared.FontSize
import com.starbucks.shared.IconPrimary
import com.starbucks.shared.MontserratFontFamily
import com.starbucks.shared.RaleWayFontFamily
import com.starbucks.shared.Resources
import com.starbucks.shared.Surface
import com.starbucks.shared.SurfaceBrand
import com.starbucks.shared.SurfaceDarker
import com.starbucks.shared.SurfaceLighter
import com.starbucks.shared.TextPrimary
import com.starbucks.shared.component.PrimaryButton
import com.starbucks.shared.component.QuantityCounter
import com.starbucks.shared.domain.ProductCategory
import com.starbucks.shared.domain.QuatityCounterSize
import com.starbucks.shared.util.DisplayResult
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    navigateBack: () -> Unit,
){
    val messageBarState = rememberMessageBarState()
    val viewModel = koinViewModel<DetailsViewModel>()
    val product by viewModel.product.collectAsState()
    val quantity = viewModel.quantity

    val sizeOrder = listOf("Short", "Tall", "Grande", "Venti")

    val selectedSize = viewModel.selectedSize
    val totalPrice = viewModel.totalPrice

    val selectedMilk = viewModel.selectedMilk
    val selectedSweetness = viewModel.selectedSweetness
    val selectedToppings = viewModel.selectedToppings
    val selectedFlavors = viewModel.selectedFlavors
    val selectedCondiment = viewModel.selectedCondiments

    var showMilkSheet by remember { mutableStateOf(false) }
    var showSweetnessSheet by remember { mutableStateOf(false) }
    var showToppingSheet by remember { mutableStateOf(false) }
    var showFlavorSheet by remember { mutableStateOf(false) }
    var showCondimentSheet by remember { mutableStateOf(false) }

    val milkOptions = listOf(
        "Default" to 0.0,
        "Non-Fat" to 0.0,
        "Coconut Milk" to 15.0,
        "Soy" to 15.0,
        "Almond Milk" to 15.0,
        "Oat Milk" to 15.0
    )

    val sweetnessOptions = listOf(
        "Default" to null,
        "Extra Sweet" to null,
        "Less Sweet" to null,
        "No Sweet" to null
    )

    val toppingOptions = listOf(
        "Caramel Sauce" to 15.0,
        "Honey Glazed Walnut topping" to 15.0,
        "Mocha Sauce" to 15.0,
        "Whip Cream" to 15.0
    )

    val flavorOptions = listOf(
        "Brown Sugar Syrup" to 15.0,
        "Caramel Flavor" to 15.0,
        "Hazelnut Flavor" to 15.0,
        "Roasted Walnut Flavored Syrup" to 15.0,
        "Salted Caramel Flavor" to 15.0,
        "Vanilla Flavor" to 15.0
    )

    val condimentOptions = listOf(
        "Coffee Jelly" to 25.0,
        "Earlgrey Jelly" to 25.0,
    )


    if (showToppingSheet) {
        BottomSheet(
            label = "EXTRA TOPPING",
            options = toppingOptions,
            selected = selectedToppings,
            onSelectedChange = { name ->
                viewModel.toggleTopping(name)
            },
            onDismiss = { showToppingSheet = false }
        )
    }

    if (showFlavorSheet) {
        BottomSheet(
            label = "EXTRA FLAVOR/SYRUP",
            options = flavorOptions,
            selected = selectedFlavors,
            onSelectedChange = { name ->
                viewModel.toggleFlavor(name)
            },
            onDismiss = { showFlavorSheet = false }
        )
    }

    if (showCondimentSheet) {
        BottomSheet(
            label = "EXTRA CONDIMENT",
            options = condimentOptions,
            selected = selectedCondiment,
            onSelectedChange = { name ->
                viewModel.toggleCondiment(name)
            },
            onDismiss = { showCondimentSheet = false }
        )
    }


    Scaffold (
        containerColor = Surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Details",
//                        text = when (product) {
//                            is RequestState.Success -> (product as RequestState.Success<Product>).data.title
//                            else -> "Details"
//                        },
                        fontFamily = RaleWayFontFamily(),
                        fontSize = FontSize.EXTRA_MEDIUM,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack){
                        Icon(
                            painter = painterResource(Resources.Icon.BackArrow),
                            contentDescription = "Back Arrow icon",
                            tint = IconPrimary
                        )
                    }
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = Surface,
                        scrolledContainerColor = Surface,
                        navigationIconContentColor = IconPrimary,
                        titleContentColor = TextPrimary,
                        actionIconContentColor = IconPrimary
                    ),
            )
        }
    ){ padding ->
        product.DisplayResult(
            onLoading = {},
            onSuccess = { selectedProduct ->
                ContentWithMessageBar(
                    modifier = Modifier
                        .padding(
                            top = padding.calculateTopPadding(),
                            bottom = padding.calculateBottomPadding()
                        ),
                    contentBackgroundColor = Surface,
                    messageBarState = messageBarState,
                    errorMaxLines = 2
                ) {
                    Column {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 24.dp)
                                .padding(top = 12.dp)
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(
                                        width = 1.dp,
                                        color = BorderIdle,
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                model = ImageRequest.Builder(LocalPlatformContext.current)
                                    .data(selectedProduct.thumbnail)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Product Image",
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            if(selectedProduct.category != ProductCategory.FOOD){
                                Text(
                                    text = selectedProduct.title,
                                    fontSize = FontSize.EXTRA_MEDIUM,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = MontserratFontFamily(),
                                    color = TextPrimary,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            } else {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.Top,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = selectedProduct.title,
                                        fontSize = FontSize.EXTRA_MEDIUM,
                                        fontWeight = FontWeight.SemiBold,
                                        fontFamily = MontserratFontFamily(),
                                        color = TextPrimary,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "฿ ${selectedProduct.price}",
                                        fontSize = FontSize.EXTRA_MEDIUM,
                                        fontWeight = FontWeight.SemiBold,
                                        fontFamily = MontserratFontFamily(),
                                        color = TextPrimary,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = selectedProduct.description,
                                fontSize = FontSize.REGULAR,
                                lineHeight = FontSize.REGULAR * 1.3,
                                color = TextPrimary
                            )
                            HorizontalDivider(
                                color = Color.Gray.copy(alpha = Alpha.HALF),
                                thickness = 1.dp,
                                modifier = Modifier.padding(vertical = 12.dp)
                            )

                            if (selectedProduct.sizes?.isNotEmpty() == true) {
                                Text(
                                    text = "SIZE",
                                    color = TextPrimary,
                                    fontSize = FontSize.REGULAR,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    sizeOrder.forEach { orderName ->
                                        selectedProduct.sizes
                                            ?.find { it.name == orderName }
                                            ?.let { size ->
                                                SizeSelector(
                                                    size = size,
                                                    isSelected = selectedSize?.name == size.name,
                                                    onSelect = { viewModel.updateSize(it) }

                                                )
                                            }
                                    }
                                }
                                HorizontalDivider(
                                    color = Color.Gray.copy(alpha = Alpha.HALF),
                                    thickness = 1.dp,
                                    modifier = Modifier.padding(vertical = 12.dp)
                                )
                            }

                            Text(
                                text = "CUSTOMIZATIONS",
                                color = TextPrimary,
                                fontSize = FontSize.REGULAR,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            if(selectedProduct.category == ProductCategory.BEVERAGE){
                                if(selectedProduct.isCoffeeShot){
                                    Stepper(
                                        placeholder = "Add Espresso",
                                        value = viewModel.shotCountEspresso,
                                        pricePerUnit = 20.0,
                                        onValueChange = { newValue, _ ->
                                            viewModel.updateEspressoShot(newValue)
                                        }
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Stepper(
                                        placeholder = "Add Half Decaf",
                                        value = viewModel.shotCountHalfDecaf,
                                        pricePerUnit = 20.0,
                                        onValueChange = { newValue, _ ->
                                            viewModel.updateHalfDecaf(newValue)
                                        }
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Stepper(
                                        placeholder = "Add Decaf",
                                        value = viewModel.shotCountDecaf,
                                        pricePerUnit = 20.0,
                                        onValueChange = { newValue, _ ->
                                            viewModel.updateDecaf(newValue)
                                        }
                                    )
                                }
                                if (selectedProduct.isMilk) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            text = "Milk",
                                            fontSize = FontSize.REGULAR,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = MontserratFontFamily(),
                                            color = TextPrimary.copy(alpha = Alpha.HALF)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(8.dp))
                                                .border(1.dp, BorderIdle, RoundedCornerShape(8.dp))
                                                .padding(12.dp)
                                                .clickable { showMilkSheet = true },
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            val selectedText = milkOptions.find { it.first == selectedMilk }?.let { (name, price) ->
                                                if (price > 0.0) "$name (฿ $price)" else name
                                            } ?: "Select"

                                            Text(
                                                text = selectedText,
                                                color = TextPrimary
                                            )
                                            Icon(
                                                imageVector = Icons.Default.ArrowDropDown,
                                                contentDescription = null,
                                                tint = IconPrimary
                                            )
                                        }
                                    }
                                }


                                if (showMilkSheet) {
                                    SingleSelectBottomSheet(
                                        options = milkOptions,
                                        label = "Milk",
                                        selectedOption = selectedMilk,
                                        onOptionSelected = { selected ->
                                            viewModel.updateMilk(selected.first)
                                        },
                                        onDismiss = { showMilkSheet = false }
                                    )
                                }

                                if (selectedProduct.isSweetness) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            text = "Sweetness",
                                            fontSize = FontSize.REGULAR,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = MontserratFontFamily(),
                                            color = TextPrimary.copy(alpha = Alpha.HALF)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(8.dp))
                                                .border(1.dp, BorderIdle, RoundedCornerShape(8.dp))
                                                .padding(12.dp)
                                                .clickable { showSweetnessSheet = true },
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = selectedSweetness ?: "Select",
                                                color = TextPrimary
                                            )
                                            Icon(
                                                imageVector = Icons.Default.ArrowDropDown,
                                                contentDescription = null,
                                                tint = IconPrimary
                                            )
                                        }
                                    }
                                }

                                if (showSweetnessSheet) {
                                    SingleSelectBottomSheet(
                                        options = sweetnessOptions,
                                        label = "Sweetness",
                                        selectedOption = selectedSweetness,
                                        onOptionSelected = { selected ->
                                            viewModel.updateSweetness(selected.first)
                                        },
                                        onDismiss = { showSweetnessSheet = false }
                                    )
                                }

                                if (selectedProduct.isFlavorAndSyrup) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            text = "Flavor/Syrup",
                                            fontSize = FontSize.REGULAR,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = MontserratFontFamily(),
                                            color = TextPrimary.copy(alpha = Alpha.HALF)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(8.dp))
                                                .border(1.dp, BorderIdle, RoundedCornerShape(8.dp))
                                                .padding(12.dp)
                                                .clickable { showFlavorSheet = true },
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            val selectedFlavorText = if (selectedFlavors.isEmpty()) {
                                                "Select"
                                            } else {
                                                flavorOptions.filter { selectedFlavors.contains(it.first) }
                                                    .joinToString { (name, price) ->
                                                        if (price > 0.0) "$name (฿ $price)" else name
                                                    }
                                            }

                                            Text(
                                                text = selectedFlavorText,
                                                color = TextPrimary
                                            )
                                            Icon(
                                                imageVector = Icons.Default.ArrowDropDown,
                                                contentDescription = null,
                                                tint = IconPrimary
                                            )
                                        }
                                    }
                                }

                                if (selectedProduct.isCondiment) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            text = "Condiment",
                                            fontSize = FontSize.REGULAR,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = MontserratFontFamily(),
                                            color = TextPrimary.copy(alpha = Alpha.HALF)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(8.dp))
                                                .border(1.dp, BorderIdle, RoundedCornerShape(8.dp))
                                                .padding(12.dp)
                                                .clickable { showCondimentSheet = true },
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            val selectedCondimentText = if (selectedCondiment.isEmpty()) {
                                                "Select"
                                            } else {
                                                condimentOptions.filter { selectedCondiment.contains(it.first) }
                                                    .joinToString { (name, price) ->
                                                        if (price > 0.0) "$name (฿ $price)" else name
                                                    }
                                            }

                                            Text(
                                                text = selectedCondimentText,
                                                color = TextPrimary
                                            )
                                            Icon(
                                                imageVector = Icons.Default.ArrowDropDown,
                                                contentDescription = null,
                                                tint = IconPrimary
                                            )
                                        }
                                    }
                                }

                                if (selectedProduct.isToppings) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            text = "Topping",
                                            fontSize = FontSize.REGULAR,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = MontserratFontFamily(),
                                            color = TextPrimary.copy(alpha = Alpha.HALF)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(8.dp))
                                                .border(1.dp, BorderIdle, RoundedCornerShape(8.dp))
                                                .padding(12.dp)
                                                .clickable { showToppingSheet = true },
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            val selectedToppingText = if (selectedToppings.isEmpty()) {
                                                "Select"
                                            } else {
                                                toppingOptions.filter { selectedToppings.contains(it.first) }
                                                    .joinToString { (name, price) ->
                                                        if (price > 0.0) "$name (฿ $price)" else name
                                                    }
                                            }

                                            Text(
                                                text = selectedToppingText,
                                                color = TextPrimary
                                            )
                                            Icon(
                                                imageVector = Icons.Default.ArrowDropDown,
                                                contentDescription = null,
                                                tint = IconPrimary
                                            )
                                        }
                                    }
                                }
                            } else if (selectedProduct.category == ProductCategory.FOOD){
                                if (selectedProduct.isCutlery) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ){
                                        Text(
                                            modifier = Modifier.padding(start = 12.dp),
                                            text = "Cutlery",
                                            fontSize = FontSize.REGULAR,
                                            color = TextPrimary
                                        )
                                        Switch(
                                            checked = viewModel.cutlery,
                                            onCheckedChange = { viewModel.updateCutlery(it) },
                                            colors = SwitchDefaults.colors(
                                                checkedTrackColor = SurfaceBrand,
                                                uncheckedTrackColor = SurfaceLighter,
                                                checkedThumbColor = Surface,
                                                uncheckedThumbColor = Surface,
                                                checkedBorderColor = SurfaceBrand,
                                                uncheckedBorderColor = SurfaceDarker
                                            )
                                        )
                                    }
                                }
                                if(selectedProduct.isWarmUp) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ){
                                        Text(
                                            modifier = Modifier.padding(start = 12.dp),
                                            text = "Warm up",
                                            fontSize = FontSize.REGULAR,
                                            color = TextPrimary
                                        )
                                        Switch(
                                            checked = viewModel.warmUp,
                                            onCheckedChange = { viewModel.updateWarmUp(it) },
                                            colors = SwitchDefaults.colors(
                                                checkedTrackColor = SurfaceBrand,
                                                uncheckedTrackColor = SurfaceLighter,
                                                checkedThumbColor = Surface,
                                                uncheckedThumbColor = Surface,
                                                checkedBorderColor = SurfaceBrand,
                                                uncheckedBorderColor = SurfaceDarker
                                            )
                                        )
                                    }
                                }
                            }


                            Spacer(modifier = Modifier.height(24.dp))
                            ResetButton(viewModel = viewModel)

                        }
                        Column(
                            modifier = Modifier
                                .padding(all = 24.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(
                                12.dp,
                                Alignment.CenterVertically
                            ),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Total: ฿ ${viewModel.totalPrice}",
                                fontSize = FontSize.MEDIUM,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )

                            QuantityCounter(
                                size = QuatityCounterSize.Large,
                                value = quantity.toString(),
                                onMinusClick = {
                                    if (quantity > 1) viewModel.updateQuantity(quantity - 1)
                                },
                                onPlusClick = {
                                    //<10 == stocks have 10
                                    if (quantity < 10) viewModel.updateQuantity(quantity + 1)
                                }
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            PrimaryButton(
                                icon = Resources.Icon.ShoppingCart,
                                text = "Add to Order",
                                enabled = totalPrice > 0.0,
                                onClick = {
                                    viewModel.addItemToCart(
                                        onSuccess = { messageBarState.addSuccess("Product added to cart") },
                                        onError = { message -> messageBarState.addError(message) }
                                    )
                                }
                            )
                        }
                    }
                }
            },
            onError = {}
        )
    }
}