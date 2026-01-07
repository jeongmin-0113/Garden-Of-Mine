package com.example.gardenofmine

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.gardenofmine.ui.model.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "GardenOfMine",
    ) {
        App()
    }
}