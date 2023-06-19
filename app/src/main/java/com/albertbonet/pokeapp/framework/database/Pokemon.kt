package com.albertbonet.pokeapp.framework.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity
@TypeConverters(Converters::class)
class Pokemon(
    @PrimaryKey
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

    @Entity
    class Type(
        val slot: Int,
        val type: SingularType
    )

    @Entity
    class SingularType(
        val name: String
    )

    @Entity
    class Stat(
        val stat: SingularStat,
        val effort: Int,
        val baseStat: Int
    )

    @Entity
    class SingularStat(
        val name: String,
        val url: String
    )

    @Entity
    class Sprite(
        val frontDefaultUrl: String
    )
}