package org.example.project.di

import org.example.project.domain.MyRepo
import org.example.project.domain.MyRepoImpl
import org.example.project.presentation.MyViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    singleOf(::MyRepoImpl).bind<MyRepo>()
    viewModelOf(::MyViewModel)
}