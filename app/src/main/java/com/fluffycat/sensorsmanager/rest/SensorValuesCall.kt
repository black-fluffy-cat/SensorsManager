package com.fluffycat.sensorsmanager.rest

import com.fluffycat.sensorsmanager.sensors.SensorValues
import okhttp3.ResponseBody
import retrofit2.Callback

class SensorValuesCall {

    private val retrofitClient = RetrofitClientFactory().createRetrofit()

    private val sensorValuesAPI: SensorValuesAPI = retrofitClient.create(SensorValuesAPI::class.java)

    fun postSensorValues(sensorValues: SensorValues, callback: Callback<ResponseBody>) =
        sensorValuesAPI.postSensorValues(sensorValues).enqueue(callback)
}