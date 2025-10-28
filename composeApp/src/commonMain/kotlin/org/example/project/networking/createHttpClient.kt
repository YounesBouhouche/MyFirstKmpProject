package org.example.project.networking

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createHttpClient(engine: HttpClientEngine, logger: Logger): HttpClient {
    return HttpClient(engine) {
        install(Logging) {
            level = LogLevel.ALL
            this.logger = logger
        }
        install(ContentNegotiation) {
            json(
                json = Json {
                    ignoreUnknownKeys = true
                },
                contentType = ContentType.Application.Json
            )
        }
        install(DefaultRequest) {
//            header("Authorization", Ub3uj44LIt1YMcZhY7W9bAIpChXIJDLuCmnfTlJruzHD4W37fqL3q32g)
            header("Authorization", AppSecrets.apiKey)
            header(HttpHeaders.Accept, ContentType.Application.Json)
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }
    }
}