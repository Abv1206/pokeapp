package com.albertbonet.pokeapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

class RemoteResultList(
    @SerializedName("count") val count: Int,
    val page: Int,
    @SerializedName("results") val results: List<Pokemons>,
) {
}

@Parcelize
data class Pokemons(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
) : Parcelable {

    fun getImageUrl(): String {
        val index = url.split("/".toRegex()).dropLast(1).last()
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$index.png"
    }
}