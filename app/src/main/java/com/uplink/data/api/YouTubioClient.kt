package com.uplink.data.api

import com.uplink.data.api.dto.CatalogDto
import com.uplink.data.api.dto.ManifestDto
import com.uplink.data.api.dto.MetaDto
import com.uplink.data.api.dto.StreamDto
import retrofit2.http.GET
import retrofit2.http.Path

// {config} is a required, dynamic per-install YouTubio token — never
// hardcoded (see PreferenceKeys.YOUTUBIO_CONFIG). Base URL is also
// user-supplied (see PreferenceKeys.YOUTUBIO_URL) and configured on
// the Retrofit instance itself (see NetworkModule), not here.
interface YouTubioClient {

    @GET("{config}/manifest.json")
    suspend fun manifest(@Path("config") config: String): ManifestDto

    @GET("{config}/catalog/{type}/{id}.json")
    suspend fun catalog(
        @Path("config") config: String,
        @Path("type") type: String,
        @Path("id") id: String
    ): CatalogDto

    @GET("{config}/meta/{type}/{id}.json")
    suspend fun meta(
        @Path("config") config: String,
        @Path("type") type: String,
        @Path("id") id: String
    ): MetaDto

    @GET("{config}/stream/{type}/{id}.json")
    suspend fun stream(
        @Path("config") config: String,
        @Path("type") type: String,
        @Path("id") id: String
    ): StreamDto
}
