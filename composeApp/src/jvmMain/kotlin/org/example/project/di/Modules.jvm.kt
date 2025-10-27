package org.example.project.di

import org.example.project.dependencies.DbClient
import org.example.project.dependencies.MyViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

actual val platformModule = module {
    singleOf(::DbClient)
}