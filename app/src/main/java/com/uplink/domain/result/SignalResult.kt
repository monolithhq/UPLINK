package com.uplink.domain.result

// Wraps repository outcomes so "we reached YouTubio and it told us
// about a broken signal" is distinguishable from "we couldn't reach
// YouTubio at all." A Signal.status of ERROR/UNAVAILABLE describes
// the signal itself; this sealed class describes whether the fetch
// that got us that Signal succeeded.
//
// Commit 004 change: NetworkError now wraps the raw Throwable instead
// of a pre-formatted message string. Retrofit/OkHttp naturally produce
// exception types, Debug Console can inspect stack/cause data later,
// and the domain/repository layer shouldn't pre-flatten errors into
// UI strings — that's a UI concern (see displayLabel-style mapping
// at call sites: NetworkError -> "LINK UNREACHABLE: " + (exception.message
// ?: "unknown error")).
sealed class SignalResult<out T> {

    data class Success<T>(
        val data: T
    ) : SignalResult<T>()

    data class NetworkError(
        val exception: Throwable
    ) : SignalResult<Nothing>()

    data class NotFound(
        val id: String
    ) : SignalResult<Nothing>()

    data class BackendError(
        val code: Int,
        val message: String?
    ) : SignalResult<Nothing>()
}
