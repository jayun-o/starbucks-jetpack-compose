package com.starbucks.profile.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.starbucks.map.MapTextField
import com.starbucks.profile.ProfileViewModel
import com.starbucks.shared.BorderError
import com.starbucks.shared.FontSize
import com.starbucks.shared.LanguageManager
import com.starbucks.shared.LocalizedStrings
import com.starbucks.shared.component.CustomTextField
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileForm(
    modifier: Modifier = Modifier,
    navigateToMap: () -> Unit,
    firstName: String,
    onFirstNameChange: (String) -> Unit,
    lastName: String,
    onLastNameChange: (String) -> Unit,
    email: String,
    address: String?,
    onAddressChange: (String) -> Unit,
    location: String?,
    onLocationChange: (String) -> Unit,
    postalCode: String?,
    onPostalCodeChange: (String) -> Unit,
    phoneNumber: String?,
    onPhoneNumberChange: (String) -> Unit,

){
    val currentLanguage by LanguageManager.language.collectAsState()
    val phoneError = phoneNumber?.let {
        when {
            it.isEmpty() -> null
            it.length < 10 -> LocalizedStrings.get("isvalid_phone_number_10", currentLanguage)
            !it.all { ch -> ch.isDigit() } -> LocalizedStrings.get("isvalid_phone_number_digit", currentLanguage)
            else -> null
        }
    }

    Column (
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        CustomTextField(
            value = firstName,
            onValueChange = onFirstNameChange,
            placeholder = LocalizedStrings.get("first_name", currentLanguage),
            error = firstName.length !in 3..50
        )
        CustomTextField(
            value = lastName,
            onValueChange = onLastNameChange,
            placeholder = LocalizedStrings.get("last_name", currentLanguage),
            error = lastName.length !in 3..50
        )
        CustomTextField(
            value = email,
            onValueChange = {},
            placeholder = LocalizedStrings.get("email", currentLanguage),
            enabled = false
        )
        CustomTextField(
            value = address ?: "",
            onValueChange = onAddressChange,
            placeholder = LocalizedStrings.get("address", currentLanguage),
            error = address?.length !in 3..50
        )

        MapTextField(
            value = location ?: "",
            modifier = Modifier.clickable { navigateToMap() },
            onValueChange = onLocationChange,
            placeholder = LocalizedStrings.get("location", currentLanguage),
            enabled = false
        )

        CustomTextField(
            value = phoneNumber ?: "",
            onValueChange = onPhoneNumberChange,
            placeholder = LocalizedStrings.get("phone_number", currentLanguage),
            error = phoneError != null,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )

        if (phoneError != null) {
            Text(
                text = phoneError,
                color = BorderError,
                fontSize = FontSize.SMALL,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}
