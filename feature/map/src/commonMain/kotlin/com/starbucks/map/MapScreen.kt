package com.starbucks.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.starbucks.shared.FontSize
import com.starbucks.shared.IconPrimary
import com.starbucks.shared.LanguageManager
import com.starbucks.shared.LocalizedStrings
import com.starbucks.shared.RaleWayFontFamily
import com.starbucks.shared.Resources
import com.starbucks.shared.Surface
import com.starbucks.shared.TextPrimary
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    navigateBack: () -> Unit,
    onLocationSelected: (String) -> Unit
) {
    val viewModel = koinViewModel<MapViewModel>()
    val screenState = viewModel.screenState
    val currentLanguage by LanguageManager.language.collectAsState()

    if(screenState.permissionDenied){
        LaunchedEffect(Unit){ navigateBack() }
        return
    }

    Scaffold(
        containerColor = Surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = LocalizedStrings.get("location", currentLanguage),
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Surface,
                    scrolledContainerColor = Surface,
                    navigationIconContentColor = IconPrimary,
                    titleContentColor = TextPrimary,
                    actionIconContentColor = IconPrimary
                )
            )
        }
    ) {
        if (screenState.loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            screenState.userCoordinates?.let { userCoords ->
                GoogleMaps(
                    userLocation = userCoords,
                    onLocationPicked = { coords, address ->
                        viewModel.selectLocation(coords, address)
                        onLocationSelected(address)
                    }
                )
            }
        }
    }
}
