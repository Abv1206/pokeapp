package com.albertbonet.pokeapp.model

import com.google.gson.annotations.SerializedName

class RemoteResult(
    val pokemon: Pokemon
) {

    class Pokemon(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("weight") val weight: Int,
        @SerializedName("height") val height: Int,
        @SerializedName("base_experience") val baseExperience: Int,
        @SerializedName("types") val types: List<Type>
    ) {

        class TypeResult(
            @SerializedName("slot") val slot: Int,
            @SerializedName("type") val types: List<Type>
        )

        class Type(
            @SerializedName("name") val name: String
        )
    }

}
