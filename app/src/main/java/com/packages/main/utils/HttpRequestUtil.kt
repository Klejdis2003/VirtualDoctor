package com.packages.main.utils

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.content.TextContent
import io.ktor.http.path
import io.ktor.util.StringValues
import io.ktor.util.valuesOf
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

abstract class HttpRequestUtil {
    companion object{
        private const val HOST= "192.168.0.164:8080"

        val json = Json { ignoreUnknownKeys = true }
        private val client = HttpClient()

        /**
         * @param path the path of the request
         * @return HttpResponse object from the server
         */

        suspend inline fun <reified T> get(path: String, params: StringValues = valuesOf(), body: Any? = null): T? {
            return getJsonFromRequest(path, params, body)?.let { json.decodeFromString(it) }
        }

        suspend inline fun <reified T> post(path: String, bodyContent: String): T? {
            return getJsonFromPostRequest(path, bodyContent)?.let { json.decodeFromString(it) }
        }
        private suspend fun makeGetRequest(path: String, params: StringValues, body: Any? = null): HttpResponse{
            val response: HttpResponse = client.get{
                        url{
                            protocol = URLProtocol.HTTP
                            host = HOST
                            path(path)
                            parameters.appendAll(params)
                            if(body != null)
                                setBody(TextContent(text = json.encodeToString(body), contentType = ContentType.Application.Json))
                        }
            }
            return response
        }

        /**
         * @param path the path of the request
         * @param bodyContent body content of the request, has to be JSON or method will fail
         * @return HttpResponse object from the server
         */
        suspend fun makePostRequest(path: String, bodyContent: String): HttpResponse{
            val response: HttpResponse = client.post{
                        url{
                            protocol = URLProtocol.HTTP
                            host = HOST
                            path(path)
                        }
                        setBody(TextContent(text = bodyContent, contentType = ContentType.Application.Json))
                    }
            return response
        }

        suspend fun makeDeleteRequest(path: String, hostAddress: String = HOST): HttpResponse{
            lateinit var response: HttpResponse
            val client = HttpClient()
                    response = client.delete{
                        url{
                            protocol = URLProtocol.HTTP
                            host = hostAddress
                            path(path)
                }
            }
            return response
        }
        /**
         * Makes a GET request to the provided address.
         * @param path the path of the request
         * @return JSON string from the server
         */
        suspend fun getJsonFromRequest(path: String, params: StringValues = valuesOf(), body: Any? = null): String? {
            val response = makeGetRequest(path, params, body)
            return when (response.status.value) {
                200 -> return response.body<String>()
                500 -> throw Exception("Server error while fetching data")
                else -> null
            }
        }

        /**
         * Makes a POST request to the provided address.
         * @param path the path of the request
         * @param bodyContent body content of the request, has to be JSON or method will fail
         * @return JSON string from the server
         */
        suspend fun getJsonFromPostRequest(path: String, bodyContent: String): String?{
            val response = makePostRequest(path, bodyContent)
            return if (response.status.value == 200)
                response.body<String>()
            else
                null
        }

    }
}