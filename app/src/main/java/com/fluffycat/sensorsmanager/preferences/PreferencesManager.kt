package com.fluffycat.sensorsmanager.preferences

import android.content.Context
import android.content.SharedPreferences
import com.fluffycat.sensorsmanager.SensorsManagerApplication
import com.fluffycat.sensorsmanager.values.AngleUnit
import com.fluffycat.sensorsmanager.values.DistanceUnit
import com.fluffycat.sensorsmanager.values.TemperatureUnit

private const val PREFERENCES_NAME = "PREFERENCES_NAME"

private const val ANGLE_UNIT_KEY = "ANGLE_UNIT_KEY"
private const val TEMPERATURE_UNIT_KEY = "TEMPERATURE_UNIT_KEY"
private const val DISTANCE_UNIT_KEY = "DISTANCE_UNIT_KEY"

class PreferencesManager {

    private fun getSharedPreferences(): SharedPreferences {
        val context = SensorsManagerApplication.getContext()
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun saveChosenAngleUnit(value: Int) {
        getSharedPreferences().edit().putInt(ANGLE_UNIT_KEY, value).apply()
    }

    fun readChosenAngleUnit(): AngleUnit {
        val defValue = AngleUnit.RADIAN.toString()
        val prefsValue = getSharedPreferences().getString(ANGLE_UNIT_KEY, defValue)
        return AngleUnit.valueOf(prefsValue ?: defValue)
    }

    fun saveChosenTemperatureUnit(value: Int) {
        getSharedPreferences().edit().putInt(TEMPERATURE_UNIT_KEY, value).apply()
    }

    fun readChosenTemperatureUnit(): TemperatureUnit {
        val defValue = TemperatureUnit.CELSIUS.toString()
        val prefsValue = getSharedPreferences().getString(TEMPERATURE_UNIT_KEY, defValue)
        return TemperatureUnit.valueOf(prefsValue ?: defValue)
    }

    fun saveChosenDistanceUnit(unit: DistanceUnit) {
        getSharedPreferences().edit().putString(DISTANCE_UNIT_KEY, unit.toString()).apply()
    }

    fun readChosenDistanceUnit(): DistanceUnit {
        val defValue = DistanceUnit.METERS.toString()
        val prefsValue = getSharedPreferences().getString(DISTANCE_UNIT_KEY, defValue)
        return DistanceUnit.valueOf(prefsValue ?: defValue)
    }
}