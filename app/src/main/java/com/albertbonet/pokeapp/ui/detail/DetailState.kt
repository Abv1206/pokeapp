package com.albertbonet.pokeapp.ui.detail

import android.content.Context
import android.widget.ImageView
import com.albertbonet.pokeapp.R
import com.albertbonet.pokeapp.model.Error
import com.albertbonet.pokeapp.model.database.Pokemon
import com.albertbonet.pokeapp.model.getPokemonBackground
import com.albertbonet.pokeapp.ui.common.showDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.gpu.BrightnessFilterTransformation

class DetailState(
    private val context: Context
) {

    fun setDetailBackground(imageView: ImageView, pokemon: Pokemon?) = with(pokemon) {
        val multi = MultiTransformation(
            BlurTransformation(8, 2),
            //BrightnessFilterTransformation(-0.1f)
        )
        Glide.with(context).load(getPokemonBackground(this?.types?.get(0)?.type))
            .apply(RequestOptions.bitmapTransform(multi))
            .into(imageView)
    }

    fun showError(error: Error) = (when (error) {
        Error.Connectivity -> "Connection error"
        is Error.NullPointer -> "Null pointer error"
        is Error.Server -> "Server error"
        is Error.SocketTimeout -> "Connection timeout error"
        is Error.Unknown -> "Unknown error"
    }).apply {
        showDialog(context, this)
    }
}