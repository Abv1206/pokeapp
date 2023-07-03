package com.albertbonet.pokeapp.domain

data class Pokemon(
    val id: Int,
    val name: String,
    val weight: Int,
    val height: Int,
    val baseExperience: Int,
    val types: List<Type>,
    val summary: String,
    val stats: List<Stat>,
    val sprites: Sprite
) {

    class Type(
        val slot: Int,
        val type: SingularType
    )

    class SingularType(
        val name: String
    )

    class Stat(
        val stat: SingularStat,
        val effort: Int,
        val baseStat: Int
    )

    class SingularStat(
        val name: String,
        val url: String
    )

    class Sprite(
        val frontDefaultUrl: String
    )
}