package com.example.logifitappp.di.components

import com.example.logifitappp.core.wearebles.WearableManager
import com.example.logifitappp.di.modules.NetworkModule
import com.example.logifitappp.di.modules.RepositoryModule
import com.example.logifitappp.di.modules.ServiceModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, RepositoryModule::class, ServiceModule::class])
interface WearableComponent {
    fun inject(target: WearableManager)
}