package com.albertbonet.pokeapp.ui.detail

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import com.albertbonet.pokeapp.model.Pokemon

class PokemonDetailInfoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
    ): AppCompatTextView(context, attrs, defStyleAttr) {

        fun setPokemon(pokemon: Pokemon) = with(pokemon) {
            text = buildSpannedString {

                bold { append("Pokédex nº: ") }
                appendLine(id.toString())

                bold { append("Weight: ") }
                appendLine(weight.toString())

                bold { append("Height: ") }
                appendLine(height.toString())

                bold { append("Base experience: ") }
                appendLine(baseExperience.toString())

                bold { append("Type 1: ") }
                appendLine(types[0].type.name)

                if (types.size > 1) {
                    bold { append("Type 2: ") }
                    appendLine(types[1].type.name)
                }
            }
        }
}