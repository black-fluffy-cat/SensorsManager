package com.fluffycat.sensorsmanager.sensors

import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.os.Build
import android.util.Log
import com.fluffycat.sensorsmanager.listeners.UniversalSensorListener
import com.fluffycat.sensorsmanager.utils.BufferedMutableSharedFlow
import com.fluffycat.sensorsmanager.utils.tag

class SensorController(private val sensorManager: SensorManager, private val sensorType: Int) : ISensorController {

    override val sensorCurrentData = BufferedMutableSharedFlow<SensorEvent?>()
    override val additionalData = BufferedMutableSharedFlow<Int?>()

    private val sensorListener: UniversalSensorListener by lazy { UniversalSensorListener(this) }

    override fun startReceivingData(): Boolean {
        val sensor = sensorManager.getDefaultSensor(sensorType)
        val registerStatus = sensorManager.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_GAME)
        if (registerStatus) {
            Log.d(tag, "Started receiving data")
        } else {
            Log.d(tag, "Registering sensor failed, sensorManager: $sensorManager, registerStatus: $registerStatus")
        }
        return registerStatus
    }

    override fun stopReceivingData() {
        sensorManager.unregisterListener(sensorListener)
        Log.d(tag, "Stopped receiving data")
    }

    override fun onSensorDataReceived(event: SensorEvent) {
        Log.d("ABAB", "onSensorDataReceived, $event")
        sensorCurrentData.tryEmit(event)
    }

    override fun onAdditionalDataChanged(accuracy: Int) {
        additionalData.tryEmit(accuracy)
    }

    override fun getSensorInfo(): String {
        val sensor = sensorManager.getDefaultSensor(sensorType)
        var infoString = ""
        sensor?.apply {
            infoString += "$name\n"
//                infoString += "fifoReservedEventCount $fifoReservedEventCount\n"
//                infoString += "fifoMaxEventCount $fifoMaxEventCount\n"
            infoString += "Maximum range: $maximumRange\n"
//                infoString += "minDelay: $minDelay\n"
            infoString += "Power: $power\n"
            infoString += "Resolution: $resolution\n"
            infoString += "Type: $type\n"
            infoString += "Vendor: $vendor\n"
            infoString += "Version: $version\n"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
//                    infoString += "stringType: $stringType\n"
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    infoString += "isWakeUpSensor: $isWakeUpSensor\n"
//                    infoString += "reportingMode: $reportingMode\n"
//                    infoString += "maxDelay: $maxDelay\n"
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    infoString += "highestDirectReportRateLevel: $highestDirectReportRateLevel\n"
                infoString += "Id: $id\n"
//                    infoString += "isDynamicSensor: $isDynamicSensor\n"
//                    infoString += "isAdditionalInfoSupported: $isAdditionalInfoSupported\n"
            }
        }
        return infoString
    }
}