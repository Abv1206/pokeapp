package com.albertbonet.pokeapp.framework.datasource

import com.albertbonet.pokeapp.data.RemoteConnection
import com.albertbonet.pokeapp.data.datasource.PokemonRemoteDataSource

class PokemonServerDataSource(private val limit: Int, private val offset: Int) :
    PokemonRemoteDataSource {

    override suspend fun findPokemons(page: Int) =
        RemoteConnection.service
            .listPokemons(
                limit,
                (offset*page)
            )
}