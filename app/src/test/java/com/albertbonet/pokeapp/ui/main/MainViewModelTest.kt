package com.albertbonet.pokeapp.ui.main

import com.albertbonet.pokeapp.CoroutinesTestRule
import com.albertbonet.pokeapp.datashared.samplePokemons
import com.albertbonet.pokeapp.usecases.GetPokemonsListUseCase
import com.albertbonet.pokeapp.usecases.RequestPokemonUseCase
import com.albertbonet.pokeapp.usecases.RequestPokemonsListUseCase
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
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
class MainViewModelTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    lateinit var getPokemonsListUseCase: GetPokemonsListUseCase

    @Mock
    lateinit var requestPokemonsListUseCase: RequestPokemonsListUseCase

    @Mock
    lateinit var requestPokemonUseCase: RequestPokemonUseCase

    private lateinit var vm: MainViewModel
    private var pokemons = listOf(samplePokemons.copy(1))

    @Before
    fun setUp() {
        whenever(getPokemonsListUseCase()).thenReturn(flowOf(pokemons))
        vm = MainViewModel(requestPokemonsListUseCase, requestPokemonUseCase, getPokemonsListUseCase)
    }

    @Test
    fun `State is updated with current cached content immediately`() = runTest {

        val results = mutableListOf<MainViewModel.UiState>()
        val job = launch { vm.state.toList(results) }
        runCurrent() //execute all coroutine pending tasks and jumps to next line when finishes
        job.cancel()

        assertEquals(MainViewModel.UiState(pokemons = pokemons), results[0])
    }
}