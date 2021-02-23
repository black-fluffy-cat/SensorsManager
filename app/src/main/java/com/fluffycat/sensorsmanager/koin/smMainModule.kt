package com.fluffycat.sensorsmanager.koin

import com.fluffycat.sensorsmanager.converter.ValuesConverter
import com.fluffycat.sensorsmanager.preferences.PreferencesManager
import com.fluffycat.sensorsmanager.sensors.SensorValueProvider
import org.koin.dsl.module

val smMainModule = module {
    single { PreferencesManager() }
    single { ValuesConverter(get()) }
    single { SensorValueProvider() }
}
