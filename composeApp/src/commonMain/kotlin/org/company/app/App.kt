package org.company.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.tbib.kfirebaseconfig.KFirebaseRemoteConfig
import org.company.app.theme.AppTheme
import kotlin.time.Duration.Companion.hours

@Composable
internal fun App() = AppTheme {
    LaunchedEffect(Unit) {
        KFirebaseRemoteConfig.init(12.hours.inWholeSeconds.toInt())
    }
    val kFirebaseRemoteConfig = KFirebaseRemoteConfig()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedButton(
            onClick = {
                println(kFirebaseRemoteConfig.getInt("test"))
            }
        ) {
            Text("Fetch value")
        }
    }
}
