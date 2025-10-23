package com.starbucks.auth

import ContentWithMessageBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mmk.kmpauth.firebase.google.GoogleButtonUiContainerFirebase
import com.starbucks.auth.component.AuthButton
import com.starbucks.auth.component.GoogleButton
import com.starbucks.shared.Alpha
import com.starbucks.shared.FontSize
import com.starbucks.shared.LanguageManager
import com.starbucks.shared.LocalizedStrings
import com.starbucks.shared.Resources
import com.starbucks.shared.Surface
import com.starbucks.shared.SurfaceBrand
import com.starbucks.shared.SurfaceError
import com.starbucks.shared.TextPrimary
import com.starbucks.shared.TextWhite
import com.starbucks.shared.component.CustomTextField
import com.starbucks.shared.component.SecondaryButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState

@Composable
fun AuthScreen(
    navigateToHome: () -> Unit,
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit
){
    val scope = rememberCoroutineScope()
    val viewModel = koinViewModel<AuthViewModel>()
    val messageBarState = rememberMessageBarState()
    var emailLoadingState by remember { mutableStateOf(false)}
    var googleLoadingState by remember { mutableStateOf(false)}
    val currentLanguage by LanguageManager.language.collectAsState()
    var accountState by remember { mutableStateOf(false)}

    // State for sign up fields
    var signUpEmail by remember { mutableStateOf("") }
    var signUpPassword by remember { mutableStateOf("") }
    var signUpPasswordVisible by remember { mutableStateOf(false) }
    var signUpFirstName by remember { mutableStateOf("") }
    var signUpLastName by remember { mutableStateOf("") }

    // State for sign in fields
    var signInEmail by remember { mutableStateOf("") }
    var signInPassword by remember { mutableStateOf("") }
    var signInPasswordVisible by remember { mutableStateOf(false) }

    Scaffold { padding ->
        ContentWithMessageBar(
            contentBackgroundColor = Surface,
            modifier = Modifier.padding(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding()
            ),
            messageBarState = messageBarState,
            errorMaxLines = 2,
            errorContainerColor = SurfaceError,
            errorContentColor = TextWhite,
            successContainerColor = SurfaceBrand,
            successContentColor = TextWhite,

            ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 24.dp)
            ){
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        modifier = Modifier.fillMaxWidth().size(100.dp),
                        painter = painterResource(Resources.Image.StarbucksLogo),
                        contentDescription = "Starbucks Logo",
                        tint = Color.Unspecified
                    )
                    if (!accountState){
                        // SIGN IN VIEW
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                                .alpha(Alpha.HALF),
                            text = "Sign in",
                            textAlign = TextAlign.Center,
                            fontSize = FontSize.EXTRA_REGULAR,
                            color = TextPrimary
                        )
                        CustomTextField(
                            value = signInEmail,
                            onValueChange = { signInEmail = it },
                            placeholder = "Email",
                            enabled = !emailLoadingState
                        )
                        CustomTextField(
                            value = signInPassword,
                            onValueChange = { signInPassword = it },
                            placeholder = "Password",
                            enabled = !emailLoadingState,
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        AuthButton(
                            loading = emailLoadingState,
                            primaryText = "Sign in",
                            secondaryText = "Signing in...",
                            icon = Resources.Icon.Checkmark,
                            onClick = {
                                emailLoadingState = true
                                viewModel.signInWithEmail(
                                    email = signInEmail,
                                    password = signInPassword,
                                    onSuccess = {
                                        scope.launch {
                                            messageBarState.addSuccess("Sign in successful!")
                                            delay(1500)
                                            navigateToHome()
                                            emailLoadingState = false
                                        }
                                    },
                                    onError = { error ->
                                        messageBarState.addError(error)
                                        emailLoadingState = false
                                    }
                                )
                            }
                        )
                        HorizontalDivider(thickness = 1.dp)
                        SecondaryButton(
                            text = "Create an account",
                            contentColor = SurfaceBrand,
                            borderColor = SurfaceBrand,
                            onClick = {
                                accountState = true
                            }
                        )
                    } else {
                        // SIGN UP VIEW
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                                .alpha(Alpha.HALF),
                            text = "Sign up",
                            textAlign = TextAlign.Center,
                            fontSize = FontSize.EXTRA_REGULAR,
                            color = TextPrimary
                        )
                        CustomTextField(
                            value = signUpFirstName,
                            onValueChange = { signUpFirstName = it },
                            placeholder = "Firstname",
                            enabled = !emailLoadingState
                        )
                        CustomTextField(
                            value = signUpLastName,
                            onValueChange = { signUpLastName = it },
                            placeholder = "Lastname",
                            enabled = !emailLoadingState
                        )
                        CustomTextField(
                            value = signUpEmail,
                            onValueChange = { signUpEmail = it },
                            placeholder = "Email",
                            enabled = !emailLoadingState
                        )
                        CustomTextField(
                            value = signUpPassword,
                            onValueChange = { signUpPassword = it },
                            placeholder = "Password",
                            enabled = !emailLoadingState,
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        AuthButton(
                            loading = emailLoadingState,
                            primaryText = "Sign up",
                            secondaryText = "Creating account...",
                            icon = Resources.Icon.Person,
                            onClick = {
                                emailLoadingState = true
                                viewModel.signUpWithEmail(
                                    email = signUpEmail,
                                    password = signUpPassword,
                                    firstName = signUpFirstName,
                                    lastName = signUpLastName,
                                    onSuccess = {
                                        scope.launch {
                                            messageBarState.addSuccess("Account created successfully!")
                                            delay(1500)
                                            navigateToHome()
                                            emailLoadingState = false
                                        }
                                    },
                                    onError = { error ->
                                        messageBarState.addError(error)
                                        emailLoadingState = false
                                    }
                                )
                            }
                        )
                        HorizontalDivider(thickness = 1.dp)
                        SecondaryButton(
                            text = "I already have an account",
                            contentColor = SurfaceBrand,
                            borderColor = SurfaceBrand,
                            onClick = {
                                accountState = false
                            }
                        )
                    }
                }
                GoogleButtonUiContainerFirebase(
                    linkAccount = false,
                    onResult = { result ->
                        result.onSuccess { user ->
                            viewModel.createCustomer(
                                user = user,
                                onSuccess = {
                                    scope.launch {
                                        messageBarState.addSuccess(LocalizedStrings.get("auth_success",currentLanguage))
                                        delay(2000)
                                        navigateToHome()
                                        googleLoadingState = false
                                    }
                                },
                                onError = { message ->
                                    messageBarState.addError(message)
                                    googleLoadingState = false
                                }
                            )
                        }.onFailure { error ->
                            if(error.message?.contains("A network error") == true){
                                messageBarState.addError("Internet connection unavailable")
                            } else if(error.message?.contains("Id token is null") == true ){
                                messageBarState.addError("Sign in canceled.")
                            } else {
                                messageBarState.addError(error.message ?: "Unknown")
                            }
                            googleLoadingState = false
                        }
                    }
                ) {
                    GoogleButton(
                        loading = googleLoadingState,
                        onClick = {
                            googleLoadingState = true
                            this@GoogleButtonUiContainerFirebase.onClick()
                        }
                    )
                }
            }

        }
    }
}