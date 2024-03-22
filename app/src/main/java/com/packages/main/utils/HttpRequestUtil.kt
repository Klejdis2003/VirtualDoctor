package com.packages.main.utils

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.content.TextContent
import io.ktor.http.path
import io.ktor.util.InternalAPI
import io.ktor.util.StringValues
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import java.util.Optional

abstract class HttpRequestUtil {
    companion object{
        private const val HOST= "10.0.2.2:8080"

        val json = Json { ignoreUnknownKeys = true }
        private val client = HttpClient()

        /**
         * @param path the path of the request
         * @return HttpResponse object from the server
         */
        suspend fun makeGetRequest(path: String, params: Optional<StringValues>): HttpResponse{
            lateinit var response: HttpResponse
            runBlocking {
                launch{
                    response = client.get{
                        url{
                            protocol = URLProtocol.HTTP
                            host = HOST
                            path(path)
                            if(params.isPresent)
                                parameters.appendAll(params.get())
                        }
                    }
                }
            }
            return response
        }

        /**
         * @param path the path of the request
         * @param bodyContent body content of the request, has to be JSON or method will fail
         * @return HttpResponse object from the server
         */
        @OptIn(InternalAPI::class)
        suspend fun makePostRequest(path: String, bodyContent: String): HttpResponse{
            lateinit var response: HttpResponse
            runBlocking {
                launch {
                    response = client.post{
                        url{
                            protocol = URLProtocol.HTTP
                            host = HOST
                            path(path)
                        }
                        body = TextContent(text = bodyContent, contentType = ContentType.Application.Json)
                    }

                }
            }
            return response
        }

        suspend fun makeDeleteRequest(path: String): HttpResponse{
            lateinit var response: HttpResponse
            runBlocking {
                launch {
                    val client = HttpClient()
                    response = client.delete{
                        url{
                            protocol = URLProtocol.HTTP
                            host = HOST
                            path(path)
                        }
                    }
                }
            }
            return response
        }

        suspend fun getJsonFromRequest(path: String): String? {
            return getJsonFromRequest(path, Optional.empty())
        }

        /**
         * Makes a GET request to the provided address.
         * @param path the path of the request
         * @return JSON string from the server
         */
        suspend fun getJsonFromRequest(path: String, params: Optional<StringValues>): String?{
            val response = makeGetRequest(path, params)
            if (response.status.value == 200)
                return response.body<String>()

            return null
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