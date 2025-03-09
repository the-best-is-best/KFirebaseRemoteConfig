package io.github.tbib.kfirebaseconfig


expect class KFirebaseRemoteConfig() {
    companion object {
        fun init(interval: Double)
    }

    fun getInfo(): RemoteConfigInfo
    suspend fun fetchAndActivate(): Boolean
    fun getString(key: String): String
    fun getBoolean(key: String): Boolean
    fun getInt(key: String): Int
}

