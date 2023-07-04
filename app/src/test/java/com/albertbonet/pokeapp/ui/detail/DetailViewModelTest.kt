package com.albertbonet.pokeapp.ui.detail

import app.cash.turbine.test
import com.albertbonet.pokeapp.CoroutinesTestRule
import com.albertbonet.pokeapp.datashared.samplePokemon
import com.albertbonet.pokeapp.ui.detail.DetailViewModel.*
import com.albertbonet.pokeapp.usecases.GetPokemonUseCase
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {

    companion object {
        private const val CHARIZARD = "Charizard"
    }

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    lateinit var getPokemonUseCase: GetPokemonUseCase

    private lateinit var vm: DetailViewModel
    private val pokemon = samplePokemon.copy(6)

    @Before
    fun setup() {
        whenever(getPokemonUseCase(CHARIZARD)).thenReturn(flowOf(pokemon))
        vm = DetailViewModel(CHARIZARD, getPokemonUseCase)
    }

    @Test
    fun `UI is updated with the pokemon on start`() = runTest {

        vm.onUiReady()

        vm.state.test {
            assertEquals(UiState(), awaitItem())
            assertEquals(UiState(pokemon = pokemon), awaitItem())
            cancel()
        }
    }
}