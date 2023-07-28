package com.albertbonet.pokeapp

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.GrantPermissionRule
import com.albertbonet.pokeapp.appTestShared.FakeBluetoothDataSource
import com.albertbonet.pokeapp.appTestShared.FakeRemoteDataSource
import com.albertbonet.pokeapp.appTestShared.FakeRemoteService
import com.albertbonet.pokeapp.appTestShared.buildDatabasePokemons
import com.albertbonet.pokeapp.data.database.PokemonDao
import com.albertbonet.pokeapp.data.server.PokemonServerDataSource
import com.albertbonet.pokeapp.di.AppModule
import com.albertbonet.pokeapp.ui.main.MainActivity
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class MainInstrumentationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val bluetoothPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        "android.permission.ACCESS_FINE_LOCATION"
    )

    @get:Rule(order = 2)
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Inject
    lateinit var pokemonDao: PokemonDao

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun check_4_items_db() = runTest {
        pokemonDao.insertPokemons(buildDatabasePokemons(1,2,3,4))
        assertEquals(4, pokemonDao.pokemonCount())
    }

    @Test
    fun check_6_items_db() = runTest {
        pokemonDao.insertPokemons(buildDatabasePokemons(1,2,3,4,5,6))
        assertEquals(6, pokemonDao.pokemonCount())
    }
}