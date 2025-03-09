package io.github.tbib.kfirebaseconfig

import kotlinx.datetime.Instant


data class RemoteConfigInfo(
    val lastFetchTime: Instant?,
    val lastFetchStatus: String,
    val minimumFetchInterval: Long?
)