package com.packages.main.utils

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.content.TextContent
import io.ktor.http.path
import io.ktor.util.StringValues
import io.ktor.util.valuesOf
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


/**
 * Uses Ktor's HttpClient to make HTTP requests, but with a more concise API
    * @param hostAddress the address of the server
    * @param protocol the protocol of the server, defaults to HTTP
    * @param port the port of the server, defaults to 8080
    * @param httpClient the HttpClient object, defaults to a new instance of HttpClient
 */
class HttpClient(
    @PublishedApi internal val hostAddress: String = HOST_ADDRESS,
    @PublishedApi internal val protocol: URLProtocol = URLProtocol.HTTP,
    @PublishedApi internal val port: Int = 8080,
    @PublishedApi internal val httpClient: HttpClient = HttpClient()
) {
    val json = Json { ignoreUnknownKeys = true }

    /**
     * Builds a URL with the given path and the host, protocol, and port of the HttpClient
     * @param path the path of the URL
     * @return a URLBuilder object with the given path and the host, protocol, and port of the HttpClient
     */

    fun urlBuilder(path: String): URLBuilder.() -> Unit = {
        protocol = this@HttpClient.protocol
        host = this@HttpClient.hostAddress
        port = this@HttpClient.port
        path(path)
    }

    suspend inline fun <reified T> get(path: String, params: StringValues = valuesOf()): T? {
        val response = httpClient.get {
            url {
                this@HttpClient.urlBuilder(path)()
                parameters.appendAll(params)
            }
        }

        return if (response.status.value == 200) response.body<String>().let { json.decodeFromString<T>(it) }
        else {
            val httpResponseError = HttpResponseError(response.status.value, response.body())
            Log.w("HttpResponseError", httpResponseError.toString())
            null
        }
    }

    suspend inline fun <reified T> post(path: String, bodyObject: T): T? {
        val response = httpClient.post {
            url {
                this@HttpClient.urlBuilder(path)()
            }
            val jsonBody = json.encodeToString(bodyObject)
            setBody(TextContent(jsonBody, contentType = ContentType.Application.Json))
        }
        return if (response.status.value == 200) response.body<String>().let { json.decodeFromString<T>(it) }
        else{
            val httpResponseError = HttpResponseError(response.status.value, response.body())
            Log.w("HttpResponseError", httpResponseError.toString())
            null
        }
    }

    suspend inline fun <reified T, reified K> postWithDifferentResponseType(path: String, bodyObject: T): K? {
        val response = httpClient.post {
            url {
                this@HttpClient.urlBuilder(path)()
            }
            val jsonBody = json.encodeToString<T>(bodyObject)
            setBody(TextContent(jsonBody, contentType = ContentType.Application.Json))
            }
        return response.body<String>().let { json.decodeFromString<K>(it) }
    }
    suspend inline fun <reified T> put(path: String, bodyContent: T): T? {
        val response = httpClient.post {
            url {
                this@HttpClient.urlBuilder(path)()

            }
            val jsonBody = json.encodeToString(bodyContent)
            setBody(TextContent(jsonBody, contentType = ContentType.Application.Json))
        }
        return response.body<String>().let { json.decodeFromString<T>(it) }
    }


    suspend inline fun <reified T> delete(path: String): T? {
        val response = httpClient.delete {
            url {
                this@HttpClient.urlBuilder(path)()

            }
        }
        return response.body<String>().let { json.decodeFromString<T>(it) }
    }

    companion object {
        private const val HOST_ADDRESS = "192.168.0.164"
    }
}