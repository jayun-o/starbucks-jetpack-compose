package com.starbucks.shared

import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object LanguageManager {
    private const val KEY = "language"
    private val settings = Settings()
    private val _language = MutableStateFlow(settings.getString(KEY, "en"))

    val language: StateFlow<String> get() = _language

    fun setLanguage(lang: String) {
        settings.putString(KEY, lang)
        _language.value = lang
    }

    fun getCurrent(): String = _language.value
}
