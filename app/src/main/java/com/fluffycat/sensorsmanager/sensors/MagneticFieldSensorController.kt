package com.fluffycat.sensorsmanager.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import com.fluffycat.sensorsmanager.SensorsManagerApplication

private const val MAGNETIC_FIELD_SENSOR_TYPE = Sensor.TYPE_MAGNETIC_FIELD

class MagneticFieldSensorController(context: Context) : BaseSensorController(context, MAGNETIC_FIELD_SENSOR_TYPE) {

    companion object {
        fun doesSensorExist(): Boolean = (SensorsManagerApplication.getContext()
            .getSystemService(Context.SENSOR_SERVICE) as SensorManager).getDefaultSensor(
                MAGNETIC_FIELD_SENSOR_TYPE) != null
    }
}