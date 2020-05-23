package com.fluffycat.sensorsmanager.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import com.fluffycat.sensorsmanager.SensorsManagerApplication

private const val ROTATION_VECTOR_SENSOR_TYPE = Sensor.TYPE_ROTATION_VECTOR

class RotationVectorController(context: Context) : BaseSensorController(context, ROTATION_VECTOR_SENSOR_TYPE) {

    companion object {
        fun doesSensorExist(): Boolean = (SensorsManagerApplication.getContext()
            .getSystemService(Context.SENSOR_SERVICE) as SensorManager).getDefaultSensor(
                ROTATION_VECTOR_SENSOR_TYPE) != null
    }
}