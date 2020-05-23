package com.fluffycat.sensorsmanager.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import com.fluffycat.sensorsmanager.SensorsManagerApplication

private const val PROXIMITY_SENSOR_TYPE = Sensor.TYPE_PROXIMITY

class ProximitySensorController(context: Context) : BaseSensorController(context, PROXIMITY_SENSOR_TYPE) {

    companion object {
        fun doesSensorExist(): Boolean = (SensorsManagerApplication.getContext()
            .getSystemService(Context.SENSOR_SERVICE) as SensorManager).getDefaultSensor(
                PROXIMITY_SENSOR_TYPE) != null
    }
}