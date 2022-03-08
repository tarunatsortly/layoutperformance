package com.example.layout

import androidx.core.content.ContextCompat

object Color {
    val dark: Int
        get() = ContextCompat.getColor(applicationContext, R.color.dark)

    val dustyOrange: Int
        get() = ContextCompat.getColor(applicationContext, R.color.dusty_orange)

    val lightPlaceholderColor: Int
        get() = ContextCompat.getColor(applicationContext, R.color.light_placeholder_color)
}