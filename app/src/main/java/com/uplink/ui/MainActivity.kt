package com.uplink.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.uplink.ui.navigation.UplinkNavGraph
import com.uplink.ui.screens.BootScreen
import com.uplink.ui.screens.BootScreenEvent
import com.uplink.ui.theme.UplinkTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UplinkTheme {
                var bootComplete by remember { mutableStateOf(false) }

                if (bootComplete) {
                    UplinkNavGraph()
                } else {
                    BootScreen(
                        onEvent = { event ->
                            when (event) {
                                BootScreenEvent.Completed -> bootComplete = true
                            }
                        }
                    )
                }
            }
        }
    }
}
