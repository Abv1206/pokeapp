package com.albertbonet.pokeapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.albertbonet.pokeapp.R

class DetailActivity : AppCompatActivity() {

    companion object {
        const val POKEMON = "DetailActivity:pokemon"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
    }
}