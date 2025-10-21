package com.typhoon.starbucks

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.mmk.kmpnotifier.notification.NotificationImage
import com.mmk.kmpnotifier.notification.Notifier
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import com.mmk.kmpnotifier.permission.permissionUtil
import com.starbucks.shared.util.IntentHandler
import com.starbucks.shared.util.PreferencesRepository
import org.koin.android.ext.android.inject
import kotlin.random.Random

class MainActivity : ComponentActivity() {

//    private val intentHandler: IntentHandler by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            )
        )
        super.onCreate(savedInstanceState)
        NotifierManager.initialize(
            configuration = NotificationPlatformConfiguration.Android(
                notificationIconResId = R.drawable.ic_launcher_foreground,
                showPushNotification = false,
            )
        )

//        val notifier = NotifierManager.getLocalNotifier()
//        notifier.notify {
//            id= Random.nextInt(0, Int.MAX_VALUE)
//            title = "Test notification"
//            body = "Body message"
//            payloadData = mapOf(
//                Notifier.KEY_URL to "https://github.com/mirzemehdi/KMPNotifier/",
//                "extraKey" to "randomValue"
//            )
//            image = NotificationImage.Url("https://github.com/user-attachments/assets/a0f38159-b31d-4a47-97a7-cc230e15d30b")
//        }

        val permissionUtil by permissionUtil()
        permissionUtil.askNotificationPermission()
        setContent {
            App()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val uri = intent.data

        println("INTENT TRIGGERED: $uri")

        val isSuccess = uri?.getQueryParameter("success")
        val isCancelled= uri?.getQueryParameter("cancel")
        val token = uri?.getQueryParameter("token")

        PreferencesRepository.savePayPalData(
            isSuccess = isSuccess?.toBooleanStrictOrNull(),
            error = if (isCancelled == "null") null
            else "Payment has been canceled.",
            token = token
        )

//        intentHandler.navigateToPaymentCompleted(
//            isSuccess = isSuccess?.toBooleanStrictOrNull(),
//            error = if (isCancelled == "null") null
//            else "Payment has been canceled.",
//            token = token
//        )
    }
}