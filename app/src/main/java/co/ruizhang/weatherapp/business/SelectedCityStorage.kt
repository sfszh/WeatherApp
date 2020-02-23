package co.ruizhang.weatherapp.business

import io.reactivex.Single

interface SelectedCityStorage {
    fun add(selectedCityModel: SelectedCityModel): Single<List<SelectedCityModel>>
    fun remove(id: String): Single<List<SelectedCityModel>>
    fun get(): Single<List<SelectedCityModel>>
}


class SelectedCityStorageImpl : SelectedCityStorage {
    private val hashMap: HashMap<String, SelectedCityModel> = hashMapOf()
    override fun add(selectedCityModel: SelectedCityModel): Single<List<SelectedCityModel>> {
        hashMap[selectedCityModel.id] = selectedCityModel
        return Single.just(hashMap.values.toList())
    }

    override fun remove(id: String): Single<List<SelectedCityModel>> {
        hashMap.remove(id)
        return Single.just(hashMap.values.toList())
    }

    override fun get(): Single<List<SelectedCityModel>> {
        return Single.just(hashMap.values.toList())
    }
}