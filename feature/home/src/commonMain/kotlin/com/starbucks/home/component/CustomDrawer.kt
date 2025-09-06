package com.starbucks.home.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.starbucks.home.domain.DrawerItem
import com.starbucks.shared.LanguageManager
import com.starbucks.shared.LocalizedStrings
import com.starbucks.shared.Resources
import com.starbucks.shared.domain.Customer
import com.starbucks.shared.util.RequestState
import org.jetbrains.compose.resources.painterResource

@Composable
fun CustomDrawer(
    customer: RequestState<Customer>,
    onProfileClick: () -> Unit,
    onContactUsClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onAdminPanelClick: () -> Unit
) {
    val currentLanguage by LanguageManager.language.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.6f)
            .padding(horizontal = 12.dp),
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Icon(
            modifier = Modifier
                .fillMaxWidth()
                .size(60.dp),
            painter = painterResource(Resources.Image.StarbucksLogo),
            contentDescription = "Logo",
            tint = Color.Unspecified
        )
        Spacer(modifier = Modifier.height(50.dp))

        DrawerItem.entries.take(5).forEach { item ->
            DrawerItemCard(
                drawerItem = item,
                title = LocalizedStrings.get(item.titleKey, currentLanguage),
                onClick = {
                    when (item) {
                        DrawerItem.Profile -> onProfileClick()
                        DrawerItem.Contact -> onContactUsClick()
                        DrawerItem.SignOut -> onSignOutClick()
                        else -> {}
                    }
                },
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        AnimatedContent(targetState = customer){ customerState ->
            if(customerState.isSuccess() && customerState.getSuccessData().isAdmin){
                DrawerItemCard(
                    drawerItem = DrawerItem.Admin,
                    onClick = onAdminPanelClick,
                    title = LocalizedStrings.get(DrawerItem.Admin.titleKey, currentLanguage)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
