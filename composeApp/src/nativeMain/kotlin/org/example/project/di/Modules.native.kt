package org.example.project.di

import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.Logger
import org.example.project.domain.DbClient
import org.example.project.presentation.MyViewModel
import org.example.project.data.networking.PexelsClient
import org.example.project.data.networking.createHttpClient
import org.example.project.domain.DbClient
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

actual val platformModule = module {
    singleOf(::DbClient)
    viewModelOf(::MyViewModel)
    single {
        PexelsClient(createHttpClient(Darwin.create(), Logger.DEFAULT))
    }
}