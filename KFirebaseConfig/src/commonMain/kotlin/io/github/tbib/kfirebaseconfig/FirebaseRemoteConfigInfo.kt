package io.github.tbib.kfirebaseconfig

import kotlin.time.ExperimentalTime
import kotlin.time.Instant


data class RemoteConfigInfo @OptIn(ExperimentalTime::class) constructor(
    val lastFetchTime: Instant?,
    val lastFetchStatus: String,
    val minimumFetchInterval: Long?
)