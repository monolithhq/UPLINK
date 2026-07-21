package com.uplink.domain.result

// Wraps repository outcomes so "we reached YouTubio and it told us
// about a broken signal" is distinguishable from "we couldn't reach
// YouTubio at all." A Signal.status of ERROR/UNAVAILABLE describes
// the signal itself; this sealed class describes whether the fetch
// that got us that Signal succeeded.
sealed class SignalResult<out T> {

    data class Success<T>(
        val data: T
    ) : SignalResult<T>()

    data class NetworkError(
        val message: String
    ) : SignalResult<Nothing>()

    data class NotFound(
        val id: String
    ) : SignalResult<Nothing>()

    data class BackendError(
        val code: Int,
        val message: String
    ) : SignalResult<Nothing>()
}
