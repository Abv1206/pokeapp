package com.albertbonet.pokeapp.ui.detail

import app.cash.turbine.test
import com.albertbonet.pokeapp.CoroutinesTestRule
import com.albertbonet.pokeapp.data.PokemonsRepository
import com.albertbonet.pokeapp.datashared.samplePokemon
import com.albertbonet.pokeapp.domain.Pokemon
import com.albertbonet.pokeapp.domain.Pokemons
import com.albertbonet.pokeapp.appTestShared.FakeBluetoothDataSource
import com.albertbonet.pokeapp.appTestShared.FakeLocalDataSource
import com.albertbonet.pokeapp.appTestShared.FakeRemoteDataSource
import com.albertbonet.pokeapp.usecases.GetPokemonUseCase
import com.albertbonet.pokeapp.usecases.RequestBluetoothConnectionUseCase
import com.albertbonet.pokeapp.usecases.SendPokemonBluetoothUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class DetailIntegrationTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun `Pokemon is loaded from local source when available`() = runTest {
        val vm = buildViewModelWith("Charizard")
        vm.onUiReady()
        vm.state.test {
            assertEquals(DetailViewModel.UiState(), awaitItem())
            assertEquals(DetailViewModel.UiState(pokemon = samplePokemon.copy(6)), awaitItem())
            cancel()
        }
    }

    private fun buildViewModelWith(
        pokemonName: String,
        localData: List<Pokemons> = emptyList(),
        remoteData: List<Pokemons> = emptyList(),
        bluetoothData: Pokemon = samplePokemon
    ): DetailViewModel {
        val localDataSource = FakeLocalDataSource().apply { pokemons.value = localData }
        val remoteDataSource = FakeRemoteDataSource().apply { pokemons = remoteData }
        val bluetoothDataSource = FakeBluetoothDataSource().apply { pokemon = bluetoothData }
        val pokemonsRepository = PokemonsRepository(localDataSource, remoteDataSource, bluetoothDataSource)

        val getPokemonUseCase = GetPokemonUseCase(pokemonsRepository)
        val requestBluetoothConnectionUseCase = RequestBluetoothConnectionUseCase(pokemonsRepository)
        val sendPokemonBluetoothUseCase = SendPokemonBluetoothUseCase(pokemonsRepository)
        return DetailViewModel(pokemonName, getPokemonUseCase, sendPokemonBluetoothUseCase, requestBluetoothConnectionUseCase)
    }
}