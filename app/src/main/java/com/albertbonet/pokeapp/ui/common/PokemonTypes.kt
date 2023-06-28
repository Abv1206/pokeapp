package com.albertbonet.pokeapp.ui.common

import com.albertbonet.pokeapp.R
import com.albertbonet.pokeapp.domain.Pokemon


enum class PokemonTypes (val type: String) {
    GRASS("grass"),
    FIRE("fire"),
    WATER("water"),
    NORMAL("normal"),
    ELECTRIC("electric"),
    ICE("ice"),
    FIGHTING("fighting"),
    POISON("posion"),
    GROUND("ground"),
    FLYING("flying"),
    PSYCHIC("psychic"),
    BUG("bug"),
    ROCK("rock"),
    GHOST("ghost"),
    DARK("dark"),
    DRAGON("dragon"),
    STEEL("steel"),
    FAIRY("fairy")
}

fun Pokemon.SingularType.compareTo(type: PokemonTypes): Boolean {
    return name == type.type
}

fun getPokemonBackground(type: Pokemon.SingularType?) = when {
    type != null -> {
        if (type.compareTo(PokemonTypes.GRASS)) R.drawable.pokemon_grass_route
        else if (type.compareTo(PokemonTypes.ICE)) R.drawable.pokemon_ice_route
        else if (type.compareTo(PokemonTypes.FIRE)) R.drawable.pokemon_fire_route
        else if (type.compareTo(PokemonTypes.FLYING)) R.drawable.pokemon_fly_route
        else if (type.compareTo(PokemonTypes.WATER)) R.drawable.pokemon_water_route
        else R.drawable.pokemon_simple_route
    }
    else -> R.drawable.pokemon_simple_route
}

fun getPokemonColor(type: Pokemon.SingularType): String {
    return "type_${type.name}"
}


