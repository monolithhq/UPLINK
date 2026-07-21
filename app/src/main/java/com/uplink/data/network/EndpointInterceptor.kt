package com.uplink.data.network

import javax.inject.Inject
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response

// Rewrites every outgoing request's destination (scheme/host/port only)
// to whatever EndpointProvider currently holds, so YouTubioRepository and
// YouTubioClient can be written against a normal-looking Retrofit
// interface without knowing the base URL is swapped at runtime.
//
// Preserved untouched: encoded path segments, encoded query, HTTP method,
// headers, body. Only scheme/host/port are replaced. This keeps Retrofit's
// generated paths (e.g. /{config}/manifest.json) fully intact.
class EndpointInterceptor @Inject constructor(
    private val endpointProvider: EndpointProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val configuredUrl = endpointProvider.currentBaseUrl().toHttpUrlOrNull()
            ?: return chain.proceed(original)

        val newUrl = original.url.newBuilder()
            .scheme(configuredUrl.scheme)
            .host(configuredUrl.host)
            .port(configuredUrl.port)
            .build()

        val newRequest = original.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}
