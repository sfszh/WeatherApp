package co.ruizhang.weatherapp.views

import android.app.Application
import co.ruizhang.weatherapp.BuildConfig
import co.ruizhang.weatherapp.di.appModule
import co.ruizhang.weatherapp.di.networkModule
import co.ruizhang.weatherapp.di.persistenceModule
import com.google.android.libraries.places.api.Places
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber
import timber.log.Timber.DebugTree


class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Start Koin
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(listOf(appModule, networkModule, persistenceModule))
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }

        // Initialize the SDK
        Places.initialize(applicationContext, BuildConfig.PLACE_API_KEY)

        // Create a new Places client instance
        Places.createClient(this)
    }
}