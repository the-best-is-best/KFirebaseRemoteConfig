package io.github.tbib.kfirebaseconfig

import io.github.native.kfirebase_config.FIRRemoteConfig
import io.github.native.kfirebase_config.FIRRemoteConfigFetchAndActivateStatus
import io.github.native.kfirebase_config.FIRRemoteConfigFetchStatus
import io.github.native.kfirebase_config.FIRRemoteConfigSettings
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSDate
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.Foundation.timeIntervalSinceDate

@OptIn(ExperimentalForeignApi::class)
actual class KFirebaseRemoteConfig {
    actual companion object {
        private val remoteConfig: FIRRemoteConfig = FIRRemoteConfig.remoteConfig()

        actual fun init(interval: Double) {
            val settings = FIRRemoteConfigSettings()
            settings.minimumFetchInterval = interval
            remoteConfig.configSettings = settings
        }
    }

    actual suspend fun fetchAndActivate(): Boolean {
        return suspendCancellableCoroutine { continuation ->
            remoteConfig.fetchAndActivateWithCompletionHandler { status, _ ->
                continuation.resume(
                    status == FIRRemoteConfigFetchAndActivateStatus.FIRRemoteConfigFetchAndActivateStatusSuccessFetchedFromRemote
                ) { cause, _, _ ->
                    null
                    (cause)
                }
            }
        }
    }

    actual fun getString(key: String): String {
        return remoteConfig.configValueForKey(key).stringValue

    }

    actual fun getBoolean(key: String): Boolean {
        return remoteConfig.configValueForKey(key).boolValue
    }

    actual fun getInt(key: String): Int {
        return remoteConfig.configValueForKey(key).numberValue.intValue
    }


    actual fun getInfo(): RemoteConfigInfo {
        val settings = remoteConfig.configSettings
        val lastFetchStatus = when (remoteConfig.lastFetchStatus) {
            FIRRemoteConfigFetchStatus.FIRRemoteConfigFetchStatusSuccess -> "SUCCESS"
            FIRRemoteConfigFetchStatus.FIRRemoteConfigFetchStatusFailure -> "FAILURE"
            FIRRemoteConfigFetchStatus.FIRRemoteConfigFetchStatusThrottled -> "THROTTLED"
            else -> "UNKNOWN"
        }

        val referenceDate = NSDate.dateWithTimeIntervalSince1970(0.0) // 1 يناير 1970
        val lastFetchTimeMs =
            ((remoteConfig.lastFetchTime!!.timeIntervalSinceDate(referenceDate)) * 1000).toLong() // ✅ الحل الصحيح

        return RemoteConfigInfo(
            lastFetchTime = lastFetchTimeMs,
            lastFetchStatus = lastFetchStatus,
            minimumFetchInterval = (settings.minimumFetchInterval * 1000).toLong()
        )

    }
}
