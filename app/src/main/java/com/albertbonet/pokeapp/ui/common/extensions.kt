package com.albertbonet.pokeapp.ui.common

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.core.animation.addListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DiffUtil
import com.albertbonet.pokeapp.App
import com.albertbonet.pokeapp.domain.Error
import com.albertbonet.pokeapp.domain.tryCall
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
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
    val shimmer =
        Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
            //.setBaseColor(resources.getColor(R.color.white_semi_transparent))
            //.setHighlightColor(resources.getColor(R.color.white_semi_transparent))
            .setDuration(1800) // how long the shimmering animation takes to do one full sweep
            .setBaseAlpha(0.95f) //the alpha of the underlying children
            .setHighlightAlpha(0.82f) // the shimmer alpha amount
            .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
            .setAutoStart(true)
            .build()

    val shimmerDrawable = ShimmerDrawable().apply {
        setShimmer(shimmer)
    }

    val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

    Glide.with(context)
        .load(url)
        .placeholder(shimmerDrawable)
        //.transition(DrawableTransitionOptions.withCrossFade())
        .transition(withCrossFade(factory))
        .timeout(0)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .into(this)
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

fun View.bouncingAnimator(): Animator {
    val animator = ObjectAnimator.ofFloat(
        this,
        "translationY",
        0f,
        -100f,
        0f
    )
    animator.duration = 1800
    animator.interpolator = BounceInterpolator()
    animator.repeatCount = Animation.INFINITE
    animator.startDelay = 100
    return animator
}

fun View.rotateAnimation(): Animator {
    val degreesToRotate = 15f
    val durationAnimation = 300L
    val reverseStartDelay = 320L

    val rotationAnimator =
        ObjectAnimator.ofFloat(this, "rotation", 0f, degreesToRotate, degreesToRotate, 0f)
    rotationAnimator.duration = durationAnimation

    val reverseRotationAnimator =
        ObjectAnimator.ofFloat(this, "rotation", 0f, -degreesToRotate, -degreesToRotate, 0f)
    reverseRotationAnimator.duration = durationAnimation
    reverseRotationAnimator.startDelay = reverseStartDelay

    pivotX = width.toFloat() / 2
    pivotY = height.toFloat()

    val animatorSet = AnimatorSet()
    animatorSet.playSequentially(rotationAnimator, reverseRotationAnimator)
    animatorSet.startDelay = 200
    animatorSet.addListener(onEnd = {animatorSet.start()})

    return animatorSet
}