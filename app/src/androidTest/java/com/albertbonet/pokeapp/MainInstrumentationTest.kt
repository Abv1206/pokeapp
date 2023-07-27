package com.albertbonet.pokeapp

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.GrantPermissionRule
import com.albertbonet.pokeapp.appTestShared.FakeBluetoothDataSource
import com.albertbonet.pokeapp.appTestShared.FakeRemoteDataSource
import com.albertbonet.pokeapp.appTestShared.FakeRemoteService
import com.albertbonet.pokeapp.data.server.PokemonServerDataSource
import com.albertbonet.pokeapp.di.AppModule
import com.albertbonet.pokeapp.ui.main.MainActivity
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@HiltAndroidTest
@UninstallModules(AppModule::class)
class MainInstrumentationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val bluetoothPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        "android.permission.ACCESS_FINE_LOCATION"
    )

    @get:Rule(order = 2)
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @BindValue
    @JvmField
    val FakeBluetoothDataSource = FakeBluetoothDataSource()

    @BindValue
    @JvmField
    val FakeRemoteService = FakeRemoteService()

    @BindValue
    @JvmField
    val FakeRemoteDataSource = FakeRemoteDataSource()

    @Test
    fun test_it_works() {

    }
}