package com.starbucks.di

import com.starbucks.auth.AuthViewModel
import com.starbucks.data.AdminRepositoryImpl
import com.starbucks.data.CustomerRepositoryImpl
import com.starbucks.data.domain.AdminRepository
import com.starbucks.admin_panel.AdminPanelViewModel
import com.starbucks.data.domain.CustomerRepository
import com.starbucks.home.HomeGraphViewModel
import com.starbucks.profile.ProfileViewModel
import com.starbucks.manage_product.ManageProductViewModel
import com.starbucks.map.MapViewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val sharedModule = module {
    single<CustomerRepository>{ CustomerRepositoryImpl() }
    single<AdminRepository>{ AdminRepositoryImpl() }

    viewModelOf(::AuthViewModel)
    viewModelOf(::HomeGraphViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::MapViewModel)
    viewModelOf(::ManageProductViewModel)
    viewModelOf(::AdminPanelViewModel)

}

expect val targetModule: Module

fun initializeKoin(
    config: (KoinApplication.() -> Unit)? = null
){
    startKoin{
        config?.invoke(this)
        modules(sharedModule, targetModule)
    }
}