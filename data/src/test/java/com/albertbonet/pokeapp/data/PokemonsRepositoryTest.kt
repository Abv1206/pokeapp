package com.albertbonet.pokeapp.data

import com.albertbonet.pokeapp.data.datasource.PokemonLocalDataSource
import com.albertbonet.pokeapp.data.datasource.PokemonRemoteDataSource
import com.albertbonet.pokeapp.datashared.samplePokemon
import com.albertbonet.pokeapp.datashared.samplePokemons
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class PokemonsRepositoryTest {

    companion object {
        private const val CHARIZARD = "Charizard"
        private const val BLASTOISE = "Blastoise"
    }

    @Mock
    lateinit var localDataSource: PokemonLocalDataSource

    @Mock
    lateinit var remoteDataSource: PokemonRemoteDataSource

    private val localPokemons = flowOf(listOf(samplePokemons.copy(1)))
    private val localPokemon = flowOf(samplePokemon.copy(6))
    private lateinit var pokemonsRepository: PokemonsRepository

    @Before
    fun setUp() {
        whenever(localDataSource.pokemons).thenReturn(localPokemons)
        whenever(localDataSource.findByName(CHARIZARD)).thenReturn(localPokemon)
        pokemonsRepository = PokemonsRepository(localDataSource, remoteDataSource)
    }

    @Test
    fun `Pokemons are taken from local data source if available`(): Unit = runBlocking {

        val result = pokemonsRepository.pokemons

        assertEquals(localPokemons, result)
    }

    @Test
    fun `Pokemon is taken from local data source if available`(): Unit = runBlocking {

        val result = pokemonsRepository.getPokemon(CHARIZARD)

        assertEquals(localPokemon, result)
    }

    @Test
    fun `Pokemon is taken from local data source but isn't equals`(): Unit = runBlocking {

        val result = pokemonsRepository.getPokemon(BLASTOISE)

        assertNotEquals(localPokemon, result)
    }
}
