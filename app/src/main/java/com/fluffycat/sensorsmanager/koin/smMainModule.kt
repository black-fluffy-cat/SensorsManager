package com.fluffycat.sensorsmanager.koin

import com.fluffycat.sensorsmanager.converter.ValuesConverter
import com.fluffycat.sensorsmanager.preferences.PreferencesManager
import org.koin.dsl.module

val smMainModule = module {
    single { PreferencesManager() }
    single { ValuesConverter(get()) }
}
