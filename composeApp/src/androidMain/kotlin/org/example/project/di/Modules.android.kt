package org.example.project.di

import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.Logger
import org.example.project.dependencies.DbClient
import org.example.project.networking.PexelsClient
import org.example.project.networking.createHttpClient
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val platformModule = module {
    singleOf(::DbClient)
    single {
        PexelsClient(createHttpClient(OkHttp.create(), Logger.ANDROID))
    }
}