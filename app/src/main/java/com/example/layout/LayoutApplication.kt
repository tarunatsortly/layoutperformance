package com.example.layout

import android.app.Application
import android.content.Context

val applicationContext: Context
    get() = LayoutApplication.instance.applicationContext

class LayoutApplication : Application() {

    companion object {
        lateinit var instance: LayoutApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}