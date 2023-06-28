package com.albertbonet.pokeapp.data.datasource

import com.albertbonet.pokeapp.domain.Error
import com.albertbonet.pokeapp.domain.Pokemon
import com.albertbonet.pokeapp.domain.Pokemons
import kotlinx.coroutines.flow.Flow

interface PokemonLocalDataSource {
    val pokemons: Flow<List<Pokemons>>
    fun findById(id: Int): Flow<Pokemon>
    fun findByName(name: String): Flow<Pokemon>
    suspend fun exists(name: String): Boolean

    suspend fun isEmpty(): Boolean

    suspend fun save(pokemons: List<Pokemons>): Error?

    suspend fun save(pokemon: Pokemon): Error?
}


