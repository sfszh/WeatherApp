package co.ruizhang.weatherapp.views

import android.app.Application
import co.ruizhang.weatherapp.BuildConfig
import co.ruizhang.weatherapp.di.appModule
import co.ruizhang.weatherapp.di.networkModule
import com.google.android.libraries.places.api.Places
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Start Koin
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(listOf(appModule, networkModule))
        }

        // Initialize the SDK
        Places.initialize(applicationContext, BuildConfig.PLACE_API_KEY)

        // Create a new Places client instance
        Places.createClient(this)
    }
}