package com.fluffycat.sensorsmanager.rest

import com.fluffycat.sensorsmanager.sensors.SensorValues
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SensorValuesAPI {

    @POST("sensor/postValues")
    fun postSensorValues(@Body sensorValues: SensorValues): Call<ResponseBody>
}