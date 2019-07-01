package co.ruizhang.weatherapp.di

import co.ruizhang.weatherapp.business.CityListService
import co.ruizhang.weatherapp.business.CityListServiceImpl
import co.ruizhang.weatherapp.viewmodels.CityListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<CityListService> { CityListServiceImpl() }
    viewModel { CityListViewModel(get()) }
}