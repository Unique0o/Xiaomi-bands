package com.example.logifitappp.di

import com.example.logifitappp.core.wearebles.WearableManager
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, RepositoryModule::class, ServiceModule::class])
interface WearableComponent {
    fun inject(target: WearableManager)
}