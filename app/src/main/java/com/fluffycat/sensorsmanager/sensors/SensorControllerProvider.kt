package com.fluffycat.sensorsmanager.sensors

import android.content.Context
import android.hardware.SensorManager
import com.fluffycat.sensorsmanager.sensors.SensorType.*

class SensorControllerProvider(context: Context) {

    private val sensorManager: SensorManager? = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager?

    private val accelerometerSensorController by lazy { SensorController(sensorManager, Accelerometer) }
    private val gyroscopeSensorController by lazy { SensorController(sensorManager, Gyroscope) }
    private val lightSensorController by lazy { SensorController(sensorManager, Light) }
    private val linearAccelerationSensorController by lazy { SensorController(sensorManager, LinearAcceleration) }
    private val magneticFieldSensorController by lazy { SensorController(sensorManager, MagneticField) }
    private val proximitySensorController by lazy { SensorController(sensorManager, Proximity) }
    private val rotationVectorSensorController by lazy { SensorController(sensorManager, RotationVector) }
    private val heartRateSensorController by lazy { SensorController(sensorManager, HeartRate) }

    fun getSensorController(sensorType: SensorType) = when (sensorType) {
        Accelerometer -> accelerometerSensorController
        Gyroscope -> gyroscopeSensorController
        Light -> lightSensorController
        LinearAcceleration -> linearAccelerationSensorController
        MagneticField -> magneticFieldSensorController
        Proximity -> proximitySensorController
        RotationVector -> rotationVectorSensorController
        HeartRate -> heartRateSensorController
    }
}