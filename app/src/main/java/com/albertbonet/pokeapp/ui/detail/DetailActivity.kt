package com.albertbonet.pokeapp.ui.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.albertbonet.pokeapp.databinding.ActivityDetailBinding
import com.albertbonet.pokeapp.model.PokemonsRepository
import com.albertbonet.pokeapp.ui.common.app
import com.albertbonet.pokeapp.ui.common.getPokemonImageById
import com.albertbonet.pokeapp.ui.common.launchAndCollect
import com.albertbonet.pokeapp.ui.common.loadUrl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class DetailActivity : AppCompatActivity() {

    companion object {
        const val POKEMON = "DetailActivity:pokemon"
    }

    private val viewModel: DetailViewModel by viewModels {
        DetailViewModelFactory(requireNotNull(intent.getStringExtra(POKEMON)), PokemonsRepository(app))
    }
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pokemonDetailToolbar.setNavigationOnClickListener { onBackPressed() }

        with (viewModel.state) {
            diff({it.pokemon?.name}) { binding.pokemonDetailToolbar.title = it }
            diff({it.pokemon}) { binding.pokemonDetailInfo.setPokemon(it)}
            diff({it.pokemon?.id}) { binding.pokemonArtImage.loadUrl(
                getPokemonImageById(it.toString())
            ) }
            diff({it.pokemon?.summary}) { binding.pokemonDetailSummary.text = it }
        }
    }

    private fun <T, U> Flow<T>.diff(mapf: (T) -> U, body: (U) -> Unit) {
        launchAndCollect(
            flow = map(mapf).distinctUntilChanged(),
            body = body
        )
    }
}