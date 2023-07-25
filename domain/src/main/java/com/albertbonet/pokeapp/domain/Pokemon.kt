package com.albertbonet.pokeapp.domain

import java.io.Serializable

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
): Serializable {

    class Type(
        val slot: Int,
        val type: SingularType
    ): Serializable

    class SingularType(
        val name: String
    ): Serializable

    class Stat(
        val stat: SingularStat,
        val effort: Int,
        val baseStat: Int
    ): Serializable

    class SingularStat(
        val name: String,
        val url: String
    ): Serializable

    class Sprite(
        val frontDefaultUrl: String
    ): Serializable
}