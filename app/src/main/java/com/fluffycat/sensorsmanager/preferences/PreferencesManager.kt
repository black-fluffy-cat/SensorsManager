package com.fluffycat.sensorsmanager.preferences

import android.content.Context
import android.content.SharedPreferences
import com.fluffycat.sensorsmanager.SensorsManagerApplication

private const val PREFERENCES_NAME = "PREFERENCES_NAME"

private const val CHOSEN_ANGLE_UNIT_KEY = "CHOSEN_ANGLE_UNIT_KEY"
private const val CHOSEN_TEMPERATURE_UNIT_KEY = "CHOSEN_TEMPERATURE_UNIT_KEY"
private const val CHOSEN_DISTANCE_UNIT_KEY = "CHOSEN_DISTANCE_UNIT_KEY"

const val ANGLE_DEGREE_VALUE = 1000
const val ANGLE_RAD_VALUE = 1001

const val TEMPERATURE_CELSIUS_VALUE = 1002
const val TEMPERATURE_KELVIN_VALUE = 1003
const val TEMPERATURE_FAHRENHEIT_VALUE = 1004

const val DISTANCE_METERS_VALUE = 1005
const val DISTANCE_FEET_VALUE = 1006

class PreferencesManager {

    private fun getSharedPreferences(): SharedPreferences {
        val context = SensorsManagerApplication.getContext()
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun saveChosenAngleUnit(value: Int) {
        getSharedPreferences().edit().putInt(CHOSEN_ANGLE_UNIT_KEY, value).apply()
    }

    fun readChosenAngleUnit(): Int = getSharedPreferences().getInt(CHOSEN_ANGLE_UNIT_KEY, ANGLE_DEGREE_VALUE)

    fun saveChosenTemperatureUnit(value: Int) {
        getSharedPreferences().edit().putInt(CHOSEN_TEMPERATURE_UNIT_KEY, value).apply()
    }

    fun readChosenTemperatureUnit(): Int =
        getSharedPreferences().getInt(CHOSEN_TEMPERATURE_UNIT_KEY, TEMPERATURE_CELSIUS_VALUE)

    fun saveChosenDistanceUnit(value: Int) {
        getSharedPreferences().edit().putInt(CHOSEN_DISTANCE_UNIT_KEY, value).apply()
    }

    fun readChosenDistanceUnit(): Int =
        getSharedPreferences().getInt(CHOSEN_DISTANCE_UNIT_KEY, DISTANCE_METERS_VALUE)
}