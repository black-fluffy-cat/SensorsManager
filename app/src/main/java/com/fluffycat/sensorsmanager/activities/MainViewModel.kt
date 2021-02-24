package com.fluffycat.sensorsmanager.activities

import androidx.lifecycle.ViewModel
import com.fluffycat.sensorsmanager.preferences.PreferencesManager
import com.fluffycat.sensorsmanager.values.DistanceUnit

class MainViewModel(private val preferencesManager: PreferencesManager) : ViewModel() {

    fun saveChosenDistanceUnit(chosenUnit: DistanceUnit) = preferencesManager.saveChosenDistanceUnit(chosenUnit)
}