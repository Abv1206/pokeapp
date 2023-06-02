package com.albertbonet.pokeapp.model.database

import androidx.room.Entity

@Entity
class Pokemon(
    val id: Int,
    val name: String,
    val weight: Int,
    val height: Int,
    val baseExperience: Int,
    val types: List<Type>,
    val summary: String,
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val specialAttack: Int,
    val specialDefense: Int,
    val speed: Int
) {

    @Entity
    class Type(
        val slot: Int,
        val type: SingularType
    )

    @Entity
    class SingularType(
        val name: String
    )
}