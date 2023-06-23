package com.albertbonet.pokeapp.domain

class Pokemons(
    val id: Int,
    val name: String,
    val url: String,
    val officialUrlImage: String
    //@ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    //val image: ByteArray
)