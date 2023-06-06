package com.albertbonet.pokeapp.model

import android.content.res.Resources
import com.albertbonet.pokeapp.R
import com.albertbonet.pokeapp.model.database.Pokemon

enum class PokemonTypes (val type: String) {
    GRASS("grass"),
    FIRE("fire"),
    WATER("water"),
    PLANT("plant"),
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
        if (type.compareTo(PokemonTypes.GRASS)) R.drawable.pokemon_simple_route
        else if (type.compareTo(PokemonTypes.PLANT)) R.drawable.pokemon_simple_route
        else if (type.compareTo(PokemonTypes.FIRE)) R.drawable.pokemon_fire_route
        else R.drawable.pokemon_simple_route
    }
    else -> R.drawable.pokemon_simple_route
}

fun getPokemonColor(type: Pokemon.SingularType): String {
    return "R.color.type_${type.name}"
}


