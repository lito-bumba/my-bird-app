package api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.serialization.kotlinx.json.json
import model.BirdImage

class ImageApiImpl : ImageApi {

    override suspend fun getImages(): List<BirdImage> {
        return client.get {
            images("/pictures.json")
        }.body()
    }

    private val client = HttpClient {
        expectSuccess = true
        install(HttpTimeout) {
            val timeout = 30000L
            connectTimeoutMillis = timeout
            requestTimeoutMillis = timeout
        }
        install(ContentNegotiation) {
            json()
        }
    }

    private fun HttpRequestBuilder.images(path: String) {
        url(BASE_URL + path)
    }

    companion object {
        const val BASE_URL = "https://sebi.io/demo-image-api/"
    }
}