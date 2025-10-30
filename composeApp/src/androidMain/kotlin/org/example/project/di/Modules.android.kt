package org.example.project.di

import com.ketch.Ketch
import com.ketch.NotificationConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.Logger
import org.example.project.R
import org.example.project.domain.DbClient
import org.example.project.data.networking.PexelsClient
import org.example.project.data.networking.createHttpClient
import org.example.project.domain.DownloadUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module

actual val platformModule = module {
    singleOf(::DbClient)
    single {
        PexelsClient(createHttpClient(OkHttp.create(), Logger.ANDROID))
    }
    single(StringQualifier("producePath")) {
        androidContext().filesDir.resolve(dataStoreFileName).absolutePath
    }
    single {
        Ketch.builder().setNotificationConfig(
            NotificationConfig(
                enabled = true,
                channelName = "File Download",
                channelDescription = "Notify user about download status update",
                smallIcon = R.drawable.ic_launcher_foreground
            )
        ).build(androidContext())
    }
    single {
        DownloadUseCase(get(), androidContext())
    }
}