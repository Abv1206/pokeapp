package com.albertbonet.pokeapp.data.datasource

import com.albertbonet.pokeapp.data.RemoteResultList


interface PokemonRemoteDataSource {
    suspend fun findPokemons(page: Int): RemoteResultList
}


