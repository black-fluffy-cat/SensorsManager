package com.fluffycat.sensorsmanager.sensors

import android.content.Context
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fluffycat.sensorsmanager.listeners.UniversalSensorListener
import com.fluffycat.sensorsmanager.utils.tag

abstract class BaseSensorController(context: Context, private val sensorType: Int) : ISensorController {

    override val sensorCurrentData = MutableLiveData<SensorEvent>()

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val sensorListener: UniversalSensorListener by lazy { UniversalSensorListener(this) }

    override fun startReceivingData() {
        sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(sensorType),
                SensorManager.SENSOR_DELAY_GAME)
        Log.d(tag, "Started receiving data")
    }

    override fun stopReceivingData() {
        sensorManager.unregisterListener(sensorListener)
        Log.d(tag, "Stopped receiving data")
    }

    override fun onSensorDataReceived(event: SensorEvent) {
        sensorCurrentData.value = event
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