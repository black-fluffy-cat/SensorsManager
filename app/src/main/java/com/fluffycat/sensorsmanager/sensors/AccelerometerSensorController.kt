package com.fluffycat.sensorsmanager.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import com.fluffycat.sensorsmanager.SensorsManagerApplication

private const val ACCELEROMETER_SENSOR_TYPE = Sensor.TYPE_ACCELEROMETER

class AccelerometerSensorController(context: Context) : BaseSensorController(context, ACCELEROMETER_SENSOR_TYPE) {

    companion object {
        fun doesSensorExist(): Boolean = (SensorsManagerApplication.getContext()
            .getSystemService(Context.SENSOR_SERVICE) as SensorManager).getDefaultSensor(
                ACCELEROMETER_SENSOR_TYPE) != null
    }
}