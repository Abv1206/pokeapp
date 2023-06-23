package com.albertbonet.pokeapp.ui.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.albertbonet.pokeapp.databinding.ActivityDetailBinding
import com.albertbonet.pokeapp.usecases.GetPokemonUseCase
import com.albertbonet.pokeapp.data.PokemonsRepository
import com.albertbonet.pokeapp.data.database.PokemonRoomDataSource
import com.albertbonet.pokeapp.data.server.PokemonServerDataSource
import com.albertbonet.pokeapp.ui.common.app
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
        val localDataSource = PokemonRoomDataSource(app.db.pokemonDao())
        val remoteDataSource = PokemonServerDataSource(PokemonsRepository.limit, PokemonsRepository.offset)
        val repository = PokemonsRepository(localDataSource, remoteDataSource)
        DetailViewModelFactory(requireNotNull(intent.getStringExtra(POKEMON)), GetPokemonUseCase(repository))
    }
    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailState: DetailState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pokemonDetailToolbar.setNavigationOnClickListener { onBackPressed() }

        detailState = DetailState(this)

        with (viewModel.state) {
            diff({it.pokemon?.name}) { binding.toolbarTitle.text = it }
            diff({it.pokemon}) { detailState.setPokemonInfo(binding, it) }
            diff({it.pokemon?.sprites?.frontDefaultUrl}) {
                it?.let{ binding.pokemonArtImage.loadUrl(it) } }
            diff({it.error}) { it?.let { detailState.showError(it) } }
            diff({ it.pokemon?.types }) {
                detailState.setDetailBackground(binding.backgroundImageView, it)
                detailState.setChipsType(binding, it)
            }
        }

    }

    override fun onStart() {
        super.onStart()
        viewModel.onUiReady()
    }

    private fun <T, U> Flow<T>.diff(mapf: (T) -> U, body: (U) -> Unit) {
        launchAndCollect(
            flow = map(mapf).distinctUntilChanged(),
            body = body
        )
    }
}