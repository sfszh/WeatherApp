package co.ruizhang.weatherapp.viewmodels

sealed class ViewResultData<T> (val data: T?) {
    class Success<T>(t: T?) : ViewResultData<T>(t)
    class Loading<T>(t: T?) : ViewResultData<T>(t)
    class Error<T>(t: T?, val throwable: Throwable): ViewResultData<T>(t)
}