package com.albertbonet.pokeapp.model.datasource

import com.albertbonet.pokeapp.model.RemoteConnection


class PokemonRemoteDataSource(private val limit: Int, private val offset: Int) {

    suspend fun findPokemons(page: Int) =
        RemoteConnection.service
            .listPokemons(
                limit,
                (offset*page)
            )
}
