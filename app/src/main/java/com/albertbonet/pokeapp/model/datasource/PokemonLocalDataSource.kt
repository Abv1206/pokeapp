package com.albertbonet.pokeapp.model.datasource

import com.albertbonet.pokeapp.model.database.Pokemon
import com.albertbonet.pokeapp.model.database.PokemonDao
import com.albertbonet.pokeapp.model.database.Pokemons
import kotlinx.coroutines.flow.Flow

class PokemonLocalDataSource(private val pokemonDao: PokemonDao) {
    val pokemons: Flow<List<Pokemons>> = pokemonDao.getAll()

    fun findById(id: Int): Flow<Pokemon> = pokemonDao.findById(id)
    fun findByName(name: String): Flow<Pokemon> = pokemonDao.findByName(name)

    suspend fun isEmpty(): Boolean = pokemonDao.pokemonCount() == 0
    suspend fun save(pokemons: List<Pokemons>) { pokemonDao.insertPokemons(pokemons) }
    suspend fun save(pokemon: Pokemon) { pokemonDao.insertPokemon(pokemon) }
}
