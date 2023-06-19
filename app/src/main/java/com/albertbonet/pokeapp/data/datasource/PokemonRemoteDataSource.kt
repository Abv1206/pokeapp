package com.albertbonet.pokeapp.data.datasource

import com.albertbonet.pokeapp.framework.server.RemoteResultList


interface PokemonRemoteDataSource {
    suspend fun findPokemons(page: Int): RemoteResultList
}


