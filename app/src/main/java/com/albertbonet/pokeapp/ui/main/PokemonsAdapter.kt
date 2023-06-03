package com.albertbonet.pokeapp.ui.main

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.albertbonet.pokeapp.R
import com.albertbonet.pokeapp.databinding.ViewPokemonBinding
import com.albertbonet.pokeapp.model.database.Pokemons
import com.albertbonet.pokeapp.ui.common.basicDiffUtil
import com.albertbonet.pokeapp.ui.common.getPokemonImageUrl
import com.albertbonet.pokeapp.ui.common.inflate
import com.albertbonet.pokeapp.ui.common.loadUrl

class PokemonsAdapter(private val listener: (Pokemons) -> Unit) :
    ListAdapter<Pokemons, PokemonsAdapter.ViewHolder>(basicDiffUtil { old, new -> old.id == new.id }) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.view_pokemon, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pokemons = getItem(position)
        holder.bind(pokemons)
        holder.itemView.setOnClickListener { listener(pokemons) }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ViewPokemonBinding.bind(view)
        fun bind(pokemons: Pokemons) = with(binding) {
            pokemonArt.loadUrl(pokemons.officialUrlImage)
            pokemonName.text = pokemons.name
        }
    }
}