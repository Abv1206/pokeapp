package com.albertbonet.pokeapp.model

import androidx.appcompat.app.AppCompatActivity

class PokemonsRepository(activity: AppCompatActivity) {

    val limit = 20
    val offset = 20

    suspend fun findPokemons(page: Int) =
        RemoteConnection.service
            .listPokemons(
                limit,
                (offset*page)
            )

    suspend fun getPokemon(name: String) =
        RemoteConnection.service
            .pokemonDetail(name)

}