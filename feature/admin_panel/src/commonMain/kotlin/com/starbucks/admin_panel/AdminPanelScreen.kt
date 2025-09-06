package com.starbucks.admin_panel

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.starbucks.shared.ButtonPrimary
import com.starbucks.shared.FontSize
import com.starbucks.shared.IconPrimary
import com.starbucks.shared.IconWhite
import com.starbucks.shared.LanguageManager
import com.starbucks.shared.LocalizedStrings
import com.starbucks.shared.RaleWayFontFamily
import com.starbucks.shared.Resources
import com.starbucks.shared.Surface
import com.starbucks.shared.TextPrimary
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun AdminPanelScreen(
    navigateBack: () -> Unit
){
    val currentLanguage by LanguageManager.language.collectAsState()

    Scaffold (
        containerColor = Surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = LocalizedStrings.get("admin_panel",
                            currentLanguage
                        ),
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
                actions = {
                    IconButton(onClick = {}){
                        Icon(
                            painter = painterResource(Resources.Icon.Search),
                            contentDescription = "Search icon",
                            tint = IconPrimary
                        )
                    }
                },
                colors =
                    TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Surface,
                        scrolledContainerColor = Surface,
                        navigationIconContentColor = IconPrimary,
                        titleContentColor = TextPrimary,
                        actionIconContentColor = IconPrimary
                    ),
                )
                 },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {},
                    containerColor = ButtonPrimary,
                    contentColor = IconWhite,
                    content = {
                        Icon(
                            painter = painterResource(Resources.Icon.Plus),
                            contentDescription = "Add icon"
                        )
                    }
                )
            }
    ){ padding ->



    }
}
