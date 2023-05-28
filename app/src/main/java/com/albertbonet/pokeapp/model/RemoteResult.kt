package com.albertbonet.pokeapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class Pokemon(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("weight") val weight: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("base_experience") val baseExperience: Int,
    @SerializedName("types") val types: List<TypeResult>
): Parcelable {

    @Parcelize
    class TypeResult(
        @SerializedName("slot") val slot: Int,
        @SerializedName("type") val types: Type
    ): Parcelable

    @Parcelize
    class Type(
        @SerializedName("name") val name: String
    ): Parcelable
}


