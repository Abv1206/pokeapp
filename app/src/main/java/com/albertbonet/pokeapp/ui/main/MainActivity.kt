package com.albertbonet.pokeapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.albertbonet.pokeapp.databinding.ActivityMainBinding
import com.albertbonet.pokeapp.model.Pokemon
import com.albertbonet.pokeapp.model.PokemonsRepository
import com.albertbonet.pokeapp.ui.detail.DetailActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels { MainViewModelFactory(PokemonsRepository(this)) }
    private val adapter = PokemonsAdapter { viewModel.onPokemonClicked(it.name) }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recycler.adapter = adapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) { // Runs when activity starts
                // collect = observe
                viewModel.state.collect(::updateUI)
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) { // Runs when activity starts
                // collect = observe
                viewModel.events.collect {
                    when (it) {
                        is MainViewModel.UiEvent.NavigateTo -> navigateTo(it.pokemon)
                    }
                }
            }
        }
    }

    private fun updateUI(state: MainViewModel.UiState) {
        binding.progress.visibility = if(state.loading) View.VISIBLE else View.GONE
        state.pokemons?.let(adapter::submitList)
    }

    private fun navigateTo(pokemon: Pokemon) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.POKEMON, pokemon)
        startActivity(intent)
    }
}

/*
// function to avoid code repetition
fun <T> LifecycleOwner.launchAndCollect(
    flow: Flow<T>,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    body: (T) -> Unit
) {
    lifecycleScope.launch {
        repeatOnLifecycle(state) {
            flow.collect(body)
        }
    }
}

viewLifecycleOwner.launchAndCollect(viewModel.state) { binding.updateUI(it) }
viewLifecycleOwner.launchAndCollect(viewModel.events) { event ->
    when (event) {
        is MainViewModel.UiEvent.NavigateTo -> navigateTo(event.movie)
    }
 }
*/
