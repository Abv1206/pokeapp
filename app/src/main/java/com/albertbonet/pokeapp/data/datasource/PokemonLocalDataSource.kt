package com.albertbonet.pokeapp.data.datasource

import com.albertbonet.pokeapp.domain.Pokemon
import com.albertbonet.pokeapp.domain.Pokemons
import com.albertbonet.pokeapp.framework.database.Pokemon as DbPokemon
import com.albertbonet.pokeapp.framework.database.Pokemons as DbPokemons
import kotlinx.coroutines.flow.Flow

interface PokemonLocalDataSource {
    val pokemons: Flow<List<Pokemons>>
    fun findById(id: Int): Flow<Pokemon>
    fun findByName(name: String): Flow<Pokemon>

    suspend fun isEmpty(): Boolean

    suspend fun save(pokemons: List<DbPokemons>)

    suspend fun save(pokemon: DbPokemon)
}


