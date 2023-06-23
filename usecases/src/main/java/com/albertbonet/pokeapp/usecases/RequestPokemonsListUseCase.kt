package com.albertbonet.pokeapp.usecases

import com.albertbonet.pokeapp.domain.Error
import com.albertbonet.pokeapp.data.PokemonsRepository

class RequestPokemonsListUseCase(private val pokemonsRepository: PokemonsRepository) {

    suspend operator fun invoke(): Error? = pokemonsRepository.requestPokemons()
}