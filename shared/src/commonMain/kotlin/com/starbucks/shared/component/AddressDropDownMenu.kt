package com.starbucks.shared.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.starbucks.shared.LanguageManager
import com.starbucks.shared.LocalizedStrings

@Composable
fun AddressDropdownMenu(
    selectedProvince: String,
    selectedDistrict: String,
    selectedSubDistrict: String,
    postalCode: String,
    onSelectionChanged: (String, String, String, String) -> Unit
) {
    val currentLanguage by LanguageManager.language.collectAsState()

    val provinces = listOf(LocalizedStrings.get("select_province", currentLanguage), "กรุงเทพมหานคร", "เชียงใหม่")

    val districtsMap = mapOf(
        "กรุงเทพมหานคร" to listOf("เขตพระนคร", "เขตดุสิต"),
        "เชียงใหม่" to listOf("เมืองเชียงใหม่", "หางดง")
    )

    val subDistrictsMap = mapOf(
        "เขตพระนคร" to listOf("พระบรมมหาราชวัง", "วังบูรพาภิรมย์"),
        "เขตดุสิต" to listOf("ดุสิต", "วชิรพยาบาล"),
        "เมืองเชียงใหม่" to listOf("ศรีภูมิ", "ช้างม่อย"),
        "หางดง" to listOf("หนองควาย", "สันผักหวาน")
    )

    val postalCodeMap = mapOf(
        "พระบรมมหาราชวัง" to "10200",
        "วังบูรพาภิรมย์" to "10200",
        "ดุสิต" to "10300",
        "วชิรพยาบาล" to "10300",
        "ศรีภูมิ" to "50200",
        "ช้างม่อย" to "50300",
        "หนองควาย" to "50230",
        "สันผักหวาน" to "50230"
    )

    // Sync internal state with parameters, reset when parameters change
    var province by remember(selectedProvince) { mutableStateOf(selectedProvince) }
    var district by remember(selectedDistrict) { mutableStateOf(selectedDistrict) }
    var subDistrict by remember(selectedSubDistrict) { mutableStateOf(selectedSubDistrict) }
    var postal by remember(postalCode) { mutableStateOf(postalCode) }

    val districts = districtsMap[province] ?: emptyList()
    val subDistricts = subDistrictsMap[district] ?: emptyList()

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CustomDropDown(
            options = provinces,
            selectedOption = province,
            onOptionSelected = {
                province = it
                district = ""
                subDistrict = ""
                postal = ""
                onSelectionChanged(province, district, subDistrict, postal)
            },
            placeholder = LocalizedStrings.get("select_province", currentLanguage),
            error = province == LocalizedStrings.get("select_province", currentLanguage)
        )
        CustomDropDown(
            options = districts.ifEmpty { listOf(LocalizedStrings.get("select_district", currentLanguage)) },
            selectedOption = district,
            onOptionSelected = {
                district = it
                subDistrict = ""
                postal = ""
                onSelectionChanged(province, district, subDistrict, postal)
            },
            placeholder = LocalizedStrings.get("select_district", currentLanguage),
            error = district.isBlank()
        )
        CustomDropDown(
            options = subDistricts.ifEmpty { listOf(LocalizedStrings.get("select_sub_district", currentLanguage)) },
            selectedOption = subDistrict,
            onOptionSelected = {
                subDistrict = it
                postal = postalCodeMap[it] ?: ""
                onSelectionChanged(province, district, subDistrict, postal)
            },
            placeholder = LocalizedStrings.get("select_sub_district", currentLanguage),
            error = subDistrict.isBlank()
        )
        CustomTextField(
            value = postal,
            onValueChange = {},
            placeholder = LocalizedStrings.get("postal_code", currentLanguage),
            error = postal.isBlank(),
            enabled = false
        )
    }
}
