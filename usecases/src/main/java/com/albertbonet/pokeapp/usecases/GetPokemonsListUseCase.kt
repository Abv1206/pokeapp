package com.albertbonet.pokeapp.usecases

import com.albertbonet.pokeapp.data.PokemonsRepository
import javax.inject.Inject

class GetPokemonsListUseCase @Inject constructor(private val pokemonsRepository: PokemonsRepository) {

    operator fun invoke() = pokemonsRepository.pokemons
}