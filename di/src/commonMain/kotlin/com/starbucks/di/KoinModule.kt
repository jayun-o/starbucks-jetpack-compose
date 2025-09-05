package com.starbucks.di

import com.starbucks.auth.AuthViewModel
import com.starbucks.data.CustomerRepositoryImpl
import com.starbucks.data.domain.CustomerRepository
import com.starbucks.home.HomeGraphViewModel
import com.starbucks.profile.ProfileViewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val sharedModule = module {
    single<CustomerRepository>{ CustomerRepositoryImpl() }

    viewModelOf(::AuthViewModel)
    viewModelOf(::HomeGraphViewModel)
    viewModelOf(::ProfileViewModel)
}

fun initializeKoin(
    config: (KoinApplication.() -> Unit)? = null
){
    startKoin{
        config?.invoke(this)
        modules(sharedModule)
    }
}