package com.albertbonet.pokeapp.data.server

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

class RemoteResultList(
    @SerializedName("count") val count: Int,
    val page: Int,
    @SerializedName("results") val results: List<PokemonsResult>,
)

@Parcelize
data class PokemonsResult(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
) : Parcelable {

    fun getId(): Int {
        return url.split("/".toRegex()).dropLast(1).last().toInt()
    }

}