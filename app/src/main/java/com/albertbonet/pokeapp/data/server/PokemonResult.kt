package com.albertbonet.pokeapp.data.server

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class PokemonResult(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("weight") val weight: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("base_experience") val baseExperience: Int,
    @SerializedName("types") val types: List<TypeResult>,
    @SerializedName("stats") val stats: List<StatResult>,
    @SerializedName("sprites") val sprites: Sprite
): Parcelable {

    @Parcelize
    class TypeResult(
        @SerializedName("slot") val slot: Int,
        @SerializedName("type") val type: Type
    ): Parcelable

    @Parcelize
    class Type(
        @SerializedName("name") val name: String
    ): Parcelable


    @Parcelize
    class StatResult(
        @SerializedName("stat") val stat: SingularStat,
        @SerializedName("effort") val effort: Int,
        @SerializedName("base_stat") val baseStat: Int
    ): Parcelable

    @Parcelize
    class SingularStat(
        @SerializedName("name") val name: String,
        @SerializedName("url") val url: String
    ): Parcelable

    @Parcelize
    class Sprite(
        @SerializedName("front_default") val frontDefaultUrl: String
    ): Parcelable

    companion object {
        const val HP = "hp"
        const val ATTACK = "attack"
        const val DEFENSE = "defense"
        const val SPECIAL_ATTACK = "special-attack"
        const val SPECIAL_DEFENSE = "special-defense"
        const val SPEED = "speed"
    }
}


