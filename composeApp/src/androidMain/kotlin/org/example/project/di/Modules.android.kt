package org.example.project.di

import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.Logger
import org.example.project.domain.DbClient
import org.example.project.data.networking.PexelsClient
import org.example.project.data.networking.createHttpClient
import org.example.project.domain.DbClient
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val platformModule = module {
    singleOf(::DbClient)
    single {
        PexelsClient(createHttpClient(OkHttp.create(), Logger.ANDROID))
    }
}