package com.albertbonet.pokeapp.domain

data class Pokemons(
    val id: Int,
    val name: String,
    val url: String,
    val officialUrlImage: String
    //@ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    //val image: ByteArray
)