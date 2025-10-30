package org.example.project.di

import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.Logger
import org.example.project.domain.DbClient
import org.example.project.presentation.MyViewModel
import org.example.project.data.networking.PexelsClient
import org.example.project.data.networking.createHttpClient
import org.example.project.domain.DownloadUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module
import java.io.File

actual val platformModule = module {
    singleOf(::DbClient)
    viewModelOf(::MyViewModel)
    single {
        PexelsClient(createHttpClient(OkHttp.create(), Logger.DEFAULT))
    }
    single(StringQualifier("producePath")) {
        val file = File(System.getProperty("java.io.tmpdir"), dataStoreFileName)
        file.absolutePath
    }
    single {
        DownloadUseCase()
    }
}