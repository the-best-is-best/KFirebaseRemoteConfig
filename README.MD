<h1 align="center">KFirebaseRemoteConfig</h1><br>
<div align="center">
<a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
<a href="https://android-arsenal.com/api?level=23" rel="nofollow">
    <img alt="API" src="https://img.shields.io/badge/API-23%2B-brightgreen.svg?style=flat" style="max-width: 100%;">
</a>
  <img src="https://img.shields.io/badge/Platform-Android-brightgreen.svg?logo=android" alt="Badge Android" />
  <img src="https://img.shields.io/badge/iOS-15%2B-blue.svg?logo=apple" alt="iOS 15+ Badge" />

<a href="https://github.com/the-best-is-best/"><img alt="Profile" src="https://img.shields.io/badge/github-%23181717.svg?&style=for-the-badge&logo=github&logoColor=white" height="20"/></a>
</div>

<br>

### KFirebaseRemoteConfig is a Kotlin Multiplatform (KMP) library designed to provide seamless integration with Firebase Remote Config across both Android and Apple platforms. This package allows developers to manage remote configuration parameters efficiently using a unified API without duplicating code for each platform

<hr>

[![Maven Central](https://img.shields.io/maven-central/v/io.github.the-best-is-best/kfirebase-config)](https://central.sonatype.com/artifact/io.github.the-best-is-best/kfirebase-config)

KFirebaseRemoteConfig is available on `mavenCentral()`.

<br>
# KFirebaseRemoteConfig Version Compatibility

| Version       | Firebase iOS SDK   | Minimum iOS Version |
|---------------|--------------------|---------------------|
| `1.0.0-dev.1` | Firebase **v11.x** | iOS **13+**         |
| `2.0.0-dev.1` | Firebase **v12.x** | iOS **15+**         |

## Installation

```kotlin
implementation("io.github.the-best-is-best:kfirebase-config:2.1.0")
```

### androidMain

```kotlin
KFirebaseRemoteConfig.init(intervalSeconds = 3600)
```

### iOS (Using Swift Package Manager - SPM)

Make sure to add Firebase as a dependency using Swift Package Manager (SPM).

1. Open your Xcode project.
2. Navigate to `File > Add Packages...`.
3. Enter Firebase repository URL: `https://github.com/firebase/firebase-ios-sdk`
4. Choose `FirebaseRemoteConfig` and add it to your project.

### iosApp

```swift
import Firebase

FirebaseApp.configure()
```

## Usage

```kotlin
val remoteConfig = KFirebaseRemoteConfig()
val value = remoteConfig.getString("feature_flag")
```

## API

```kotlin
package io.github.kfirebase_remoteconfig

expect class KFirebaseRemoteConfig() {
    companion object {
        fun init(intervalSeconds: Int)
    }

    fun getInfo(): RemoteConfigInfo
    suspend fun fetchAndActivate(): Boolean
    fun getString(key: String): String
    fun getBoolean(key: String): Boolean
    fun getInt(key: String): Int
}
```
