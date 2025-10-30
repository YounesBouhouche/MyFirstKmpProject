package org.example.project.di

import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.Logger
import kotlinx.cinterop.ExperimentalForeignApi
import org.example.project.domain.DbClient
import org.example.project.presentation.MyViewModel
import org.example.project.data.networking.PexelsClient
import org.example.project.data.networking.createHttpClient
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
actual val platformModule = module {
    singleOf(::DbClient)
    viewModelOf(::MyViewModel)
    single {
        PexelsClient(createHttpClient(Darwin.create(), Logger.DEFAULT))
    }
    single(StringQualifier("producePath")) {
        val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        requireNotNull(documentDirectory).path + "/$dataStoreFileName"
    }
}