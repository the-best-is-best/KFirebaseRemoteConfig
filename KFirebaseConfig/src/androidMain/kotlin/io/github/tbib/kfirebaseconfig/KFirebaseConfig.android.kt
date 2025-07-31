package io.github.tbib.kfirebaseconfig

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.tasks.await
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

actual class KFirebaseRemoteConfig {
    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig

    actual companion object {

        actual fun init(intervalSeconds: Int) {
            val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
            val configSettings = remoteConfigSettings {
                minimumFetchIntervalInSeconds = intervalSeconds.seconds.inWholeSeconds
            }
            remoteConfig.setConfigSettingsAsync(configSettings)
            remoteConfig.fetchAndActivate()
        }
    }

    actual suspend fun fetchAndActivate(): Boolean {
        return remoteConfig.fetchAndActivate().await()

    }

    actual fun getString(key: String): String {
        return remoteConfig.getString(key)
    }

    actual fun getBoolean(key: String): Boolean {
        return remoteConfig.getBoolean(key)
    }

    actual fun getInt(key: String): Int {

        return remoteConfig.getLong(key).toInt()
    }

    @OptIn(ExperimentalTime::class)
    actual fun getInfo(): RemoteConfigInfo {
        val settings = remoteConfig.info.configSettings
        val lastFetchStatus = when (remoteConfig.info.lastFetchStatus) {
            FirebaseRemoteConfig.LAST_FETCH_STATUS_SUCCESS -> "SUCCESS"
            FirebaseRemoteConfig.LAST_FETCH_STATUS_FAILURE -> "FAILURE"
            FirebaseRemoteConfig.LAST_FETCH_STATUS_THROTTLED -> "THROTTLED"
            else -> "UNKNOWN"
        }
        return RemoteConfigInfo(
            lastFetchTime = Instant.fromEpochMilliseconds(remoteConfig.info.fetchTimeMillis),
            lastFetchStatus = lastFetchStatus,
            minimumFetchInterval = settings.minimumFetchIntervalInSeconds
        )
    }
}

