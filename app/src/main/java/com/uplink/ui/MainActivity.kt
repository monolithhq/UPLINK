package com.uplink.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.uplink.ui.screens.BootScreen
import com.uplink.ui.theme.UplinkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UplinkTheme {
                BootScreen()
            }
        }
    }
}
