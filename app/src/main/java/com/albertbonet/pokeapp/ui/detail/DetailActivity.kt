package com.albertbonet.pokeapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.albertbonet.pokeapp.R
import com.albertbonet.pokeapp.databinding.ActivityDetailBinding
import com.albertbonet.pokeapp.model.Pokemon
import com.albertbonet.pokeapp.ui.common.getPokemonImageById
import com.albertbonet.pokeapp.ui.common.getPokemonImageUrl
import com.albertbonet.pokeapp.ui.common.loadUrl
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    companion object {
        const val POKEMON = "DetailActivity:pokemon"
    }

    private val viewModel: DetailViewModel by viewModels {
        DetailViewModelFactory(requireNotNull(intent.getParcelableExtra(POKEMON)))
    }
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) { // Runs when activity starts
                // collect = observe
                viewModel.state.collect { updateUI(it.pokemon) }
            }
        }
    }

    private fun updateUI(pokemon: Pokemon) = with(binding) {
        pokemonDetailToolbar.title = pokemon.name
        pokemonDetailInfo.setPokemon(pokemon)
        pokemonArtImage.loadUrl(getPokemonImageById(pokemon.id.toString()))
        pokemonDetailSummary.text = "Hardcoded summary"
    }
}