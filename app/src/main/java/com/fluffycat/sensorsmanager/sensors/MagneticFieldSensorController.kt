package com.fluffycat.sensorsmanager.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fluffycat.sensorsmanager.listeners.MagneticFieldListener
import com.fluffycat.sensorsmanager.listeners.MySensorListener
import com.fluffycat.sensorsmanager.utils.tag

class MagneticFieldSensorController(context: Context) : ISensorController {

    override val sensorCurrentData = MutableLiveData<SensorEvent>()

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val magneticFieldListener: MySensorListener = MagneticFieldListener(this)

    override fun startReceivingData() {
        magneticFieldListener.registerListener(sensorManager)
        Log.d(tag, "Started receiving data")
    }

    override fun stopReceivingData() {
        sensorManager.unregisterListener(magneticFieldListener)
        Log.d(tag, "Stopped receiving data")
    }

    override fun onSensorDataReceived(event: SensorEvent) {
        sensorCurrentData.value = event
    }

    override fun getSensorInfo(): String {
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
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