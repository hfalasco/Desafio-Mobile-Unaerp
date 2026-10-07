package com.unaerp.grandlinestore

import android.app.Application
import com.unaerp.grandlinestore.di.AppContainer

class GrandLineStoreApp : Application() {

    val container: AppContainer by lazy { AppContainer() }
}
