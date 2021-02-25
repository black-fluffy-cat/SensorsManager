package com.fluffycat.sensorsmanager.koin

import com.fluffycat.sensorsmanager.values.ValuesConverter
import com.fluffycat.sensorsmanager.notification.NotificationManagerBuilder
import com.fluffycat.sensorsmanager.preferences.PreferencesManager
import com.fluffycat.sensorsmanager.rest.RetrofitClientFactory
import com.fluffycat.sensorsmanager.rest.SensorValuesCall
import com.fluffycat.sensorsmanager.sensors.SensorValueCollector
import com.fluffycat.sensorsmanager.sensors.SensorTypeProvider
import com.fluffycat.sensorsmanager.values.UnitsProvider
import org.koin.dsl.module

val smMainModule = module {
    single { PreferencesManager() }
    single { ValuesConverter(get()) }
    single { SensorTypeProvider() }
    single { RetrofitClientFactory() }
    single { SensorValueCollector(SensorValuesCall(get())) }
    single { NotificationManagerBuilder() }
    single { UnitsProvider() }
}
