package com.starbucks.di

import com.starbucks.auth.AuthViewModel
import com.starbucks.data.AdminRepositoryImpl
import com.starbucks.data.CustomerRepositoryImpl
import com.starbucks.data.domain.AdminRepository
import com.starbucks.admin_panel.AdminPanelViewModel
import com.starbucks.data.ProductRepositoryImpl
import com.starbucks.data.domain.CustomerRepository
import com.starbucks.data.domain.ProductRepository
import com.starbucks.checkout.CheckoutViewModel
import com.starbucks.home.HomeGraphViewModel
import com.starbucks.order.OrderViewModel
import com.starbucks.all_products.AllProductsViewModel
import com.starbucks.details.DetailsViewModel
import com.starbucks.cart.CartViewModel
import com.starbucks.payment_completed.PaymentViewModel
import com.starbucks.products_overview.ProductsOverviewViewModel
import com.starbucks.profile.ProfileViewModel
import com.starbucks.manage_product.ManageProductViewModel
import com.starbucks.map.MapViewModel
import com.starbucks.category_search.CategorySearchViewModel
import com.starbucks.checkout.domain.PaypalApi
import com.starbucks.data.OrderRepositoryImpl
import com.starbucks.data.domain.OrderRepository
import com.starbucks.shared.util.IntentHandler
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val sharedModule = module {
    single<CustomerRepository>{ CustomerRepositoryImpl() }
    single<AdminRepository>{ AdminRepositoryImpl() }
    single<ProductRepository>{ ProductRepositoryImpl() }
    single<OrderRepository>{ OrderRepositoryImpl(get()) }
    single<IntentHandler> { IntentHandler() }
    single<PaypalApi> { PaypalApi() }

    viewModelOf(::AuthViewModel)
    viewModelOf(::HomeGraphViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::MapViewModel)
    viewModelOf(::ManageProductViewModel)
    viewModelOf(::AdminPanelViewModel)
    viewModelOf(::ProductsOverviewViewModel)
    viewModelOf(::DetailsViewModel)
    viewModelOf(::CartViewModel)
    viewModelOf(::CategorySearchViewModel)
    viewModelOf(::AllProductsViewModel)
    viewModelOf(::CheckoutViewModel)
    viewModelOf(::PaymentViewModel)
    viewModelOf(::OrderViewModel)

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