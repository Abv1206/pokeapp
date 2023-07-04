package com.albertbonet.pokeapp.ui.detail

import app.cash.turbine.test
import com.albertbonet.pokeapp.CoroutinesTestRule
import com.albertbonet.pokeapp.data.PokemonsRepository
import com.albertbonet.pokeapp.datashared.samplePokemon
import com.albertbonet.pokeapp.domain.Pokemons
import com.albertbonet.pokeapp.ui.FakeLocalDataSource
import com.albertbonet.pokeapp.ui.FakeRemoteDataSource
import com.albertbonet.pokeapp.ui.main.MainViewModel
import com.albertbonet.pokeapp.usecases.GetPokemonUseCase
import com.albertbonet.pokeapp.usecases.GetPokemonsListUseCase
import com.albertbonet.pokeapp.usecases.RequestPokemonUseCase
import com.albertbonet.pokeapp.usecases.RequestPokemonsListUseCase
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
        remoteData: List<Pokemons> = emptyList()
    ): DetailViewModel {
        val localDataSource = FakeLocalDataSource().apply { pokemons.value = localData }
        val remoteDataSource = FakeRemoteDataSource().apply { pokemons = remoteData }
        val pokemonsRepository = PokemonsRepository(localDataSource, remoteDataSource)

        val getPokemonUseCase = GetPokemonUseCase(pokemonsRepository)
        return DetailViewModel(pokemonName, getPokemonUseCase)
    }
}