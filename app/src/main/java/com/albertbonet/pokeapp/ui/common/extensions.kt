package com.albertbonet.pokeapp.ui.common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DiffUtil
import com.albertbonet.pokeapp.App
import com.albertbonet.pokeapp.model.Error
import com.albertbonet.pokeapp.model.tryCall
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = true): View =
    LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)

inline fun <T> basicDiffUtil(
    crossinline areItemsTheSame: (T, T) -> Boolean = { old, new -> old == new },
    crossinline areContentsTheSame: (T, T) -> Boolean = { old, new -> old == new }
) = object : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        areItemsTheSame(oldItem, newItem)

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
        areContentsTheSame(oldItem, newItem)
}

fun ImageView.loadUrl(url: String): Error? = tryCall {
    Glide.with(context).load(url).timeout(10000).into(this)
}

fun getPokemonImageUrl(url: String): String {
    val index = url.split("/".toRegex()).dropLast(1).last()
    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$index.png"
}

fun getPokemonImageById(id: String): String {
    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
}


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

// casting of Application, is an extension property
val Context.app: App
    get() = applicationContext as App

fun showDialog(context: Context, message: String) {
    val alertDialogBuilder = AlertDialog.Builder(context)
    alertDialogBuilder.setMessage(message)
    alertDialogBuilder.setPositiveButton("Accept") { dialog, _ -> dialog.dismiss() }

    val alertDialog = alertDialogBuilder.create()
    alertDialog.show()
}