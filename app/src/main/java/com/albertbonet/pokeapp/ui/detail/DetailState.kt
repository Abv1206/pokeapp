package com.albertbonet.pokeapp.ui.detail

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.ImageView
import com.albertbonet.pokeapp.databinding.ActivityDetailBinding
import com.albertbonet.pokeapp.model.Error
import com.albertbonet.pokeapp.model.PokemonTypes
import com.albertbonet.pokeapp.model.compareTo
import com.albertbonet.pokeapp.model.database.Pokemon
import com.albertbonet.pokeapp.model.getPokemonBackground
import com.albertbonet.pokeapp.model.getPokemonColor
import com.albertbonet.pokeapp.ui.common.showDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.gpu.BrightnessFilterTransformation

class DetailState(
    private val context: Context
) {

    fun setDetailBackground(imageView: ImageView, pokemon: Pokemon?) = with(pokemon) {
        val multi = MultiTransformation(
            BlurTransformation(8, 2),
            BrightnessFilterTransformation(-0.1f)
        )
        Glide.with(context).load(getPokemonBackground(this?.types?.discernNormalType()))
            .transition(DrawableTransitionOptions.withCrossFade())
            .apply(RequestOptions.bitmapTransform(multi))
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
            .into(imageView)
    }

    private fun List<Pokemon.Type>?.discernNormalType(): Pokemon.SingularType? {
        // We don't want show background of Normal pokemon type if it has a second type,
        // we always show de second one
        return when {
            this != null -> {
                if (this.size == 1) {
                    this[0].type
                } else {
                    if (this[0].type.compareTo(PokemonTypes.NORMAL)) this[1].type else this[0].type
                }
            } else -> null
        }
    }

    @SuppressLint("DiscouragedApi")
    fun setChipsType(binding: ActivityDetailBinding, pokemon: Pokemon?) = when {
        pokemon != null && pokemon.types.isNotEmpty() -> {
            binding.layoutInfoPokemon.type1.visibility = View.VISIBLE
            val stringColor = getPokemonColor(pokemon.types[0].type)
            val colorId = context.resources.getIdentifier(stringColor, "color", context.packageName)
            binding.layoutInfoPokemon.type1.setChipBackgroundColorResource(colorId)
            binding.layoutInfoPokemon.type1.text = pokemon.types[0].type.name

            if (pokemon.types.size > 1) {
                binding.layoutInfoPokemon.type2.visibility = View.VISIBLE
                val stringColor2 = getPokemonColor(pokemon.types[1].type)
                val colorId2 = context.resources.getIdentifier(stringColor2, "color", context.packageName)
                binding.layoutInfoPokemon.type2.setChipBackgroundColorResource(colorId2)
                binding.layoutInfoPokemon.type2.text = pokemon.types[1].type.name
            } else {
                binding.layoutInfoPokemon.type2.visibility = View.GONE
            }
        } else -> {
            binding.layoutInfoPokemon.type1.visibility = View.GONE
            binding.layoutInfoPokemon.type2.visibility = View.GONE
        }
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