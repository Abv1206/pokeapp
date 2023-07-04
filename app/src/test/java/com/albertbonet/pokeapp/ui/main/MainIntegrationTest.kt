package com.albertbonet.pokeapp.ui.main

import app.cash.turbine.test
import com.albertbonet.pokeapp.CoroutinesTestRule
import com.albertbonet.pokeapp.data.PokemonsRepository
import com.albertbonet.pokeapp.datashared.samplePokemons
import com.albertbonet.pokeapp.domain.Pokemons
import com.albertbonet.pokeapp.ui.FakeLocalDataSource
import com.albertbonet.pokeapp.ui.FakeRemoteDataSource
import com.albertbonet.pokeapp.usecases.GetPokemonsListUseCase
import com.albertbonet.pokeapp.usecases.RequestPokemonUseCase
import com.albertbonet.pokeapp.usecases.RequestPokemonsListUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class MainIntegrationTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun `Data is loaded from server when local source is empty`(): Unit = runTest {
        val remoteData = listOf(samplePokemons.copy(1), samplePokemons.copy(2))
        val vm = buildViewModelWith(remoteData = remoteData)
        vm.onUiReady()

        vm.state.test {
            assertEquals(MainViewModel.UiState(), awaitItem())
            assertEquals(MainViewModel.UiState(pokemons = emptyList()), awaitItem())
            assertEquals(MainViewModel.UiState(pokemons = emptyList(), loading = true), awaitItem())
            assertEquals(MainViewModel.UiState(pokemons = emptyList(), loading = false), awaitItem())
            assertEquals(MainViewModel.UiState(pokemons = remoteData, loading = false), awaitItem())
            cancel()
        }
    }

    @Test
    fun `Data is loaded from local source when available`() = runTest {
        val localData = listOf(samplePokemons.copy(3), samplePokemons.copy(4))
        val remoteData = listOf(samplePokemons.copy(1), samplePokemons.copy(2))

        val vm = buildViewModelWith(localData, remoteData)
        vm.state.test {
            assertEquals(MainViewModel.UiState(), awaitItem())
            assertEquals(MainViewModel.UiState(pokemons = localData), awaitItem())
            cancel()
        }
    }

    private fun buildViewModelWith(
        localData: List<Pokemons> = emptyList(),
        remoteData: List<Pokemons> = emptyList()
    ): MainViewModel {
        val localDataSource = FakeLocalDataSource().apply { pokemons.value = localData }
        val remoteDataSource = FakeRemoteDataSource().apply { pokemons = remoteData }
        val pokemonsRepository = PokemonsRepository(localDataSource, remoteDataSource)

        val getPokemonsListUseCase = GetPokemonsListUseCase(pokemonsRepository)
        val requestPokemonsListUseCase = RequestPokemonsListUseCase(pokemonsRepository)
        val requestPokemonUseCase = RequestPokemonUseCase(pokemonsRepository)
        return MainViewModel(requestPokemonsListUseCase, requestPokemonUseCase, getPokemonsListUseCase)
    }
}