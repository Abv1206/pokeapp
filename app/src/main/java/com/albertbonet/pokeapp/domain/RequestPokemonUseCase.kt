package com.albertbonet.pokeapp.domain

import com.albertbonet.pokeapp.model.Error
import com.albertbonet.pokeapp.model.PokemonsRepository

class RequestPokemonUseCase(private val pokemonsRepository: PokemonsRepository) {

    suspend operator fun invoke(name: String): Error? = pokemonsRepository.requestPokemon(name)
}