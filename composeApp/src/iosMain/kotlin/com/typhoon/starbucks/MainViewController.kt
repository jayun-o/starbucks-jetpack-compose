package com.typhoon.starbucks

import androidx.compose.ui.window.ComposeUIViewController
import com.starbucks.di.initializeKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initializeKoin() }
) { App() }