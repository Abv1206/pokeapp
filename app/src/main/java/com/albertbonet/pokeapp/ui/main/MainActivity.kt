package com.albertbonet.pokeapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.albertbonet.pokeapp.databinding.ActivityMainBinding
import com.albertbonet.pokeapp.ui.detail.DetailActivity

class MainActivity : AppCompatActivity(), MainPresenter.View {

    //TODO modify when new interface constructor
    private val presenter by lazy { MainPresenter(lifecycleScope) }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter.onCreate(this@MainActivity)
        //TODO add setAdapter
    }

    override fun onDestroy () {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun showProgress() {
        binding.progress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.progress.visibility = View.GONE
    }

    override fun updateData() {
        //TODO send new data to adapter
    }

    override fun navigateTo() {
        val intent = Intent(this, DetailActivity::class.java)
        //intent.putExtra(DetailActivity.POKEMON, pokemon)
        startActivity(intent)
    }
}
