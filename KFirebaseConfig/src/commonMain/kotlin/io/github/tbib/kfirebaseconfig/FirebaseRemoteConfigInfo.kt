package io.github.tbib.kfirebaseconfig


data class RemoteConfigInfo(
    val lastFetchTime: Long,
    val lastFetchStatus: String,
    val minimumFetchInterval: Long
)