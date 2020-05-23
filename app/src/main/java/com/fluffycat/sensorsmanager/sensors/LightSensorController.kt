package com.fluffycat.sensorsmanager.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import com.fluffycat.sensorsmanager.SensorsManagerApplication

private const val LIGHT_SENSOR_TYPE = Sensor.TYPE_LIGHT

class LightSensorController(context: Context) : BaseSensorController(context, LIGHT_SENSOR_TYPE) {

    companion object {
        fun doesSensorExist(): Boolean = (SensorsManagerApplication.getContext()
            .getSystemService(Context.SENSOR_SERVICE) as SensorManager).getDefaultSensor(
                LIGHT_SENSOR_TYPE) != null
    }
}