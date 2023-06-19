package com.albertbonet.pokeapp.usecases

import com.albertbonet.pokeapp.data.PokemonsRepository

class GetPokemonsListUseCase(private val pokemonsRepository: PokemonsRepository) {

    operator fun invoke() = pokemonsRepository.pokemons
}