package com.fluffycat.sensorsmanager.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import com.fluffycat.sensorsmanager.SensorsManagerApplication

private const val LINEAR_ACCELERATION_SENSOR_TYPE = Sensor.TYPE_LINEAR_ACCELERATION

class LinearAccelerationSensorController(context: Context) :
    BaseSensorController(context, LINEAR_ACCELERATION_SENSOR_TYPE) {

    companion object {
        fun doesSensorExist(): Boolean = (SensorsManagerApplication.getContext()
            .getSystemService(Context.SENSOR_SERVICE) as SensorManager).getDefaultSensor(
                LINEAR_ACCELERATION_SENSOR_TYPE) != null
    }
}