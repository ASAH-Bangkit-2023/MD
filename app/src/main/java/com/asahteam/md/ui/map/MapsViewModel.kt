package com.asahteam.md.ui.map

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.asahteam.md.remote.response.MapsResponse
import com.asahteam.md.remote.response.ResultResponse
import com.asahteam.md.repository.MapsRepository

class MapsViewModel(private val repository: MapsRepository) : ViewModel() {
    private var lat = -6.9749708
    private var lng = 107.6342167

    val maps = MediatorLiveData<ResultResponse<MapsResponse>>()

    fun setLocation(latitude: Double, longitude: Double) {
        lat = latitude
        lng = longitude
        getMaps()
    }

    private fun getMaps() {
        maps.addSource(repository.getMaps(lat, lng)) {
            maps.value = it
        }
    }
}