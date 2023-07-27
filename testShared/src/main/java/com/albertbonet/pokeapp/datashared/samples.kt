package com.albertbonet.pokeapp.datashared

import com.albertbonet.pokeapp.domain.Pokemon
import com.albertbonet.pokeapp.domain.Pokemons


val samplePokemons = Pokemons (
    1,
    "Bulbasaur",
    "http://google.com",
    "image.com"
)

val samplePokemon = Pokemon (
    6,
    "Charizard",
    1500,
    120,
    210,
    listOf(),
    "",
    listOf(),
    Pokemon.Sprite("")
        )