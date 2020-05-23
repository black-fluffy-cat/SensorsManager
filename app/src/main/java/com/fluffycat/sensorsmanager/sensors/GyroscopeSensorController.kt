package com.fluffycat.sensorsmanager.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import com.fluffycat.sensorsmanager.SensorsManagerApplication

private const val GYROSCOPE_SENSOR_TYPE = Sensor.TYPE_GYROSCOPE

class GyroscopeSensorController(context: Context) : BaseSensorController(context, GYROSCOPE_SENSOR_TYPE) {

    companion object {
        fun doesSensorExist(): Boolean = (SensorsManagerApplication.getContext()
            .getSystemService(Context.SENSOR_SERVICE) as SensorManager).getDefaultSensor(
                GYROSCOPE_SENSOR_TYPE) != null
    }
}