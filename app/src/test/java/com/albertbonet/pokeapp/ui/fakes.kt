package com.albertbonet.pokeapp.ui

import arrow.core.right
import com.albertbonet.pokeapp.data.datasource.PokemonLocalDataSource
import com.albertbonet.pokeapp.data.datasource.PokemonRemoteDataSource
import com.albertbonet.pokeapp.datashared.samplePokemon
import com.albertbonet.pokeapp.datashared.samplePokemons
import com.albertbonet.pokeapp.domain.Error
import com.albertbonet.pokeapp.domain.Pokemon
import com.albertbonet.pokeapp.domain.Pokemons
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

val defaultFakePokemons = listOf(
    samplePokemons.copy(1),
    samplePokemons.copy(2),
    samplePokemons.copy(3)
)

val defaultFakePokemon = samplePokemon.copy(1)

class FakeLocalDataSource : PokemonLocalDataSource {

    val inMemoryPokemons = MutableStateFlow<List<Pokemons>>(emptyList())
    val inMemoryPokemon = MutableStateFlow<List<Pokemon>>(emptyList())

    override val pokemons = inMemoryPokemons

    private lateinit var findPokemonFlow: MutableStateFlow<Pokemon>

    override suspend fun isEmpty() = pokemons.value.isEmpty()

    override fun findByName(name: String): Flow<Pokemon> {
        findPokemonFlow = MutableStateFlow(inMemoryPokemon.value.first { it.name == name })
        return findPokemonFlow
    }

    override fun findById(id: Int): Flow<Pokemon> = flowOf(inMemoryPokemon.value.first { it.id == id })

    override suspend fun exists(name: String): Boolean = inMemoryPokemon.value.any { it.name == name }

    override suspend fun save(pokemons: List<Pokemons>): Error? {
        inMemoryPokemons.value = pokemons
        return null
    }

    override suspend fun save(pokemon: Pokemon): Error? {
        inMemoryPokemon.value = listOf(pokemon)

        if(::findPokemonFlow.isInitialized) {
            listOf(pokemon).firstOrNull { it.name == findPokemonFlow.value.name }
                ?.let { findPokemonFlow.value = it }
        }
        return null
    }
}

class FakeRemoteDataSource : PokemonRemoteDataSource {

    var pokemons = defaultFakePokemons
    var pokemon = defaultFakePokemon

    override suspend fun findPokemons(page: Int) = pokemons.right()

    override suspend fun requestPokemon(name: String) = pokemon.right()
}