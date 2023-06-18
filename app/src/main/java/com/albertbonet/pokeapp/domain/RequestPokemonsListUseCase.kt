package com.albertbonet.pokeapp.domain

import com.albertbonet.pokeapp.model.Error
import com.albertbonet.pokeapp.model.PokemonsRepository

class RequestPokemonsListUseCase(private val pokemonsRepository: PokemonsRepository) {

    suspend operator fun invoke(): Error? = pokemonsRepository.requestPokemons()
}