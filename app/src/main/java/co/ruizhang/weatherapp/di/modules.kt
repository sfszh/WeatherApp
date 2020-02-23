package co.ruizhang.weatherapp.di

import androidx.room.Room
import co.ruizhang.weatherapp.business.*
import co.ruizhang.weatherapp.business.persistence.WeatherDatabase
import co.ruizhang.weatherapp.viewmodels.CityDetailViewModel
import co.ruizhang.weatherapp.viewmodels.CityListViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single<CityListRepository> { CityListRepositoryImpl(get(), get(), get()) }
    single<CityDetailRepository> { CityDetailRepositoryImpl(get()) }
    single<SelectedCityStorage> { SelectedCityStorageImpl() }
    single<CityWeatherStorage> { CityWeatherStorageImpl(get()) }
    viewModel { CityListViewModel(get()) }
    viewModel { CityDetailViewModel(get()) }
}

val networkModule = module {
    single { provideDefaultOkhttpClient() }
    single { provideRetrofit(get()) }
    single { provideWeatherApi(get()) }
}

val persistenceModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            WeatherDatabase::class.java, "weather-database"
        ).build()
    }
}

fun provideDefaultOkhttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .build()
}

fun provideRetrofit(client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(SERVER_BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
}

fun provideWeatherApi(retrofit: Retrofit): WeatherApi = retrofit.create(WeatherApi::class.java)

const val SERVER_BASE_URL = "https://api.openweathermap.org"