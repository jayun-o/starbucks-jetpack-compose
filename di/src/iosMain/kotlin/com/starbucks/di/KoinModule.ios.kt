package com.starbucks.di

import com.starbucks.manage_product.util.PhotoPicker
import org.koin.core.module.Module
import org.koin.dsl.module

actual val targetModule = module {
    single<PhotoPicker> { PhotoPicker() }
}