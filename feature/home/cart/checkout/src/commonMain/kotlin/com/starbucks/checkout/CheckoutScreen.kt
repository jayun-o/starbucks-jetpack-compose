package com.starbucks.checkout

import ContentWithMessageBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.starbucks.shared.FontSize
import com.starbucks.shared.MontserratFontFamily
import com.starbucks.shared.Resources
import com.starbucks.shared.Surface
import com.starbucks.shared.component.PrimaryButton
import com.starbucks.shared.component.ProfileForm
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    totalAmount: Double,
    navigateToMap: () -> Unit,
    selectedLocation: String? = null
){
    val messageBarState = rememberMessageBarState()
    val viewModel = koinViewModel<CheckoutViewModel>()
    val screenState = viewModel.screenState
    val isFormValid = viewModel.isFormValid

    LaunchedEffect(selectedLocation) {
        selectedLocation?.let { location ->
            viewModel.updateLocation(location)
        }
    }

    Scaffold (
        containerColor = Surface,
    ){ padding ->
        ContentWithMessageBar(
            contentBackgroundColor = Surface,
            modifier = Modifier
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                ),
            messageBarState = messageBarState,
            errorMaxLines = 2
        ){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .padding(top = 12.dp,bottom = 24.dp)
                    .imePadding()
            ) {
                // Scrollable form content
                ProfileForm(
                    modifier = Modifier.fillMaxSize(),
                    firstName = screenState.firstName,
                    onFirstNameChange = viewModel::updateFirstName,
                    lastName = screenState.lastName,
                    onLastNameChange = viewModel::updateLastName,
                    email = screenState.email,
                    address = screenState.address,
                    onAddressChange = viewModel::updateAddress,
                    location = screenState.location,
                    onLocationChange = viewModel::updateLocation,
                    navigateToMap = {
                        navigateToMap()
                    },
                    postalCode = screenState.postalCode,
                    onPostalCodeChange = viewModel::updatePostalCode,
                    phoneNumber = screenState.phoneNumber,
                    onPhoneNumberChange = viewModel::updatePhoneNumber,
                )

                // Fixed bottom section
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Start),
                        text = "Total: ฿$totalAmount",
                        fontFamily = MontserratFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = FontSize.MEDIUM
                    )
                    PrimaryButton(
                        text = "Pay on Delivery",
                        icon = Resources.Icon.ShoppingCart,
                        secondary = true,
                        enabled = isFormValid,
                        onClick = {
                            viewModel.payOnDelivery(
                                onSuccess = {},
                                onError = { message ->
                                    messageBarState.addError(message)
                                }
                            )
                        }
                    )
                }
            }
        }
    }
}