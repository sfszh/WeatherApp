package co.ruizhang.weatherapp.viewmodels

sealed class ResultData<T>(val data: T?) {
    class Success<T>(t: T) : ResultData<T>(t)
    class Error<T>(t: T?, val throwable: Throwable) : ResultData<T>(t)
}