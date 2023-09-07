package com.sunnyweather.android.logic

import android.util.Log
import androidx.lifecycle.liveData
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers

/**
 * 仓库层的统一封装入口
 */
object Repository {
    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
        val result = try {
            val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
            Log.d("Response", placeResponse.toString())
            if (placeResponse.status == "ok") {
                val places = placeResponse.places
                Result.success(places)
            } else {
                Result.failure(RuntimeException("response status is" +
                        placeResponse.status
                ))
            }
        } catch (e: Exception) {
            Result.failure<List<Place>>(e)
        }
        Log.d("status", result.isSuccess.toString())
        emit(result)
    }
}