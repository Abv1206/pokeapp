package com.albertbonet.pokeapp.domain

import com.albertbonet.pokeapp.model.Error
import com.albertbonet.pokeapp.model.PokemonsRepository

class GetPokemonsListUseCase(private val pokemonsRepository: PokemonsRepository) {

    operator fun invoke() = pokemonsRepository.pokemons
}