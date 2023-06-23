package com.albertbonet.pokeapp.data

import com.albertbonet.pokeapp.data.datasource.PokemonLocalDataSource
import com.albertbonet.pokeapp.data.datasource.PokemonRemoteDataSource
import com.albertbonet.pokeapp.domain.Pokemon
import com.albertbonet.pokeapp.domain.Error
import kotlinx.coroutines.flow.Flow

class PokemonsRepository(private val localDataSource: PokemonLocalDataSource,
                         private val remoteDataSource: PokemonRemoteDataSource) {

    companion object {
        val limit = 1500
        val offset = 0
    }

    val pokemons = localDataSource.pokemons

    suspend fun requestPokemons(): Error? {
        if (localDataSource.isEmpty()) {
            val pokemons = remoteDataSource.findPokemons(0)
            pokemons.fold(ifLeft = { return it }) {
                localDataSource.save(it)
            }
        }
        return null
    }

    suspend fun requestPokemon(name: String): Error? {
        if (!localDataSource.exists(name)) {
            val pokemon = remoteDataSource.requestPokemon(name)
            pokemon.fold(ifLeft = { return it }) {
                localDataSource.save(it)
            }
        }
        return null
    }

    fun getPokemon(name: String): Flow<Pokemon> = localDataSource.findByName(name)

}

