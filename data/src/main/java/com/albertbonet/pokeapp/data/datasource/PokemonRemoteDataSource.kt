package com.albertbonet.pokeapp.data.datasource

import arrow.core.Either
import com.albertbonet.pokeapp.domain.Error
import com.albertbonet.pokeapp.domain.Pokemon
import com.albertbonet.pokeapp.domain.Pokemons


interface PokemonRemoteDataSource {
    suspend fun findPokemons(page: Int): Either<Error, List<Pokemons>>

    suspend fun requestPokemon(name: String): Either<Error, Pokemon>
}


