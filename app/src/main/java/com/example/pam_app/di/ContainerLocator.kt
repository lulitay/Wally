package com.example.pam_app.di

import android.content.Context
import kotlin.jvm.Synchronized

object ContainerLocator {
    private var container: Container? = null
    fun locateComponent(context: Context?): Container? {
        if (container == null) {
            setComponent(ProductionContainer(context))
        }
        return container
    }

    fun setComponent(container: Container?) {
        ContainerLocator.container = container
    }
}