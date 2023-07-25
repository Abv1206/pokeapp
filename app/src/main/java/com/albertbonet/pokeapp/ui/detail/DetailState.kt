package com.albertbonet.pokeapp.ui.detail

import android.Manifest
import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import com.albertbonet.pokeapp.data.AndroidPermissionChecker
import com.albertbonet.pokeapp.databinding.ActivityDetailBinding
import com.albertbonet.pokeapp.domain.Error
import com.albertbonet.pokeapp.data.server.PokemonResult
import com.albertbonet.pokeapp.ui.common.PokemonTypes
import com.albertbonet.pokeapp.ui.common.compareTo
import com.albertbonet.pokeapp.ui.common.getPokemonBackground
import com.albertbonet.pokeapp.ui.common.getPokemonColor
import com.albertbonet.pokeapp.domain.Pokemon
import com.albertbonet.pokeapp.ui.common.pokedexPrefix
import com.albertbonet.pokeapp.ui.common.showDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.skydoves.progressview.ProgressView
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.gpu.BrightnessFilterTransformation

class DetailState(
    private val context: Context,
    private val bluetoothAdapter: BluetoothAdapter
) {

    private lateinit var alertDialog: AlertDialog

    private val androidPermissionChecker = AndroidPermissionChecker(context)

    private var dataList = mutableListOf<BluetoothDevice>()
    private val listAdapter: ArrayAdapter<String> by lazy {
        ArrayAdapter(context, R.layout.two_line_list_item, R.id.text1)
    }

    val PERMISSIONS_LOCATION = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_PRIVILEGED
        )
    } else {
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH_PRIVILEGED
        )
    }

    fun setDetailBackground(imageView: ImageView, types: List<Pokemon.Type>?) = with(types) {
        val multi = MultiTransformation(
            BlurTransformation(8, 2),
            BrightnessFilterTransformation(-0.1f)
        )
        Glide.with(context).load(getPokemonBackground(this.discernNormalType()))
            .transition(DrawableTransitionOptions.withCrossFade())
            .apply(RequestOptions.bitmapTransform(multi))
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
            .into(imageView)
    }

    private fun List<Pokemon.Type>?.discernNormalType(): Pokemon.SingularType? {
        // We don't want show background of Normal pokemon type if it has a second type,
        // we always show de second one
        return when {
            this != null -> {
                if (this.size == 1) {
                    this[0].type
                } else {
                    if (this[0].type.compareTo(PokemonTypes.NORMAL)) this[1].type else this[0].type
                }
            }

            else -> null
        }
    }

    fun setPokemonInfo(binding: ActivityDetailBinding, pokemon: Pokemon?) = with(pokemon) {
        when {
            this != null -> {
                binding.layoutInfoDetailPokemon.heightText.text =
                    "${(height.toFloat() / 10f)}m"
                binding.layoutInfoDetailPokemon.weightText.text =
                    "${(weight.toFloat() / 10f)}kg"
                binding.layoutInfoDetailPokemon.pokedexNumberText.text = pokedexPrefix(id)


                binding.layoutInfoDetailPokemon.hpProgressView.setBaseStats(
                    "",
                    getValueOrDefault(this.stats.find { it.stat.name == PokemonResult.HP }?.baseStat?.toFloat())
                )
                binding.layoutInfoDetailPokemon.attackProgressView.setBaseStats(
                    "",
                    getValueOrDefault(this.stats.find { it.stat.name == PokemonResult.ATTACK }?.baseStat?.toFloat())
                )
                binding.layoutInfoDetailPokemon.defenseProgressView.setBaseStats(
                    "",
                    getValueOrDefault(this.stats.find { it.stat.name == PokemonResult.DEFENSE }?.baseStat?.toFloat())
                )
                binding.layoutInfoDetailPokemon.spAttackProgressView.setBaseStats(
                    "",
                    getValueOrDefault(this.stats.find { it.stat.name == PokemonResult.SPECIAL_ATTACK }?.baseStat?.toFloat())
                )
                binding.layoutInfoDetailPokemon.spDefenseProgressView.setBaseStats(
                    "",
                    getValueOrDefault(this.stats.find { it.stat.name == PokemonResult.SPECIAL_DEFENSE }?.baseStat?.toFloat())
                )
                binding.layoutInfoDetailPokemon.speedProgressView.setBaseStats(
                    "",
                    getValueOrDefault(this.stats.find { it.stat.name == PokemonResult.SPEED }?.baseStat?.toFloat())
                )
                binding.layoutInfoDetailPokemon.expProgressView.setBaseStats(
                    "",
                    getValueOrDefault(this.baseExperience.toFloat())
                )
            }
        }
    }

    private fun ProgressView.setBaseStats(stat: String, value: Float) {
        this.progress = value
        this.labelText = "${value.toInt()}/${this.max.toInt()}"
        this.duration = 1000L
    }

    private fun getValueOrDefault(value: Float?): Float = value ?: 0f

    @SuppressLint("DiscouragedApi")
    fun setChipsType(binding: ActivityDetailBinding, types: List<Pokemon.Type>?) = when {
        !types.isNullOrEmpty() -> {
            binding.layoutInfoTypePokemon.type1.visibility = View.VISIBLE
            val stringColor = getPokemonColor(types[0].type)
            val colorId = context.resources.getIdentifier(stringColor, "color", context.packageName)
            binding.layoutInfoTypePokemon.type1.setChipBackgroundColorResource(colorId)
            binding.layoutInfoTypePokemon.type1.text = types[0].type.name

            if (types.size > 1) {
                binding.layoutInfoTypePokemon.type2.visibility = View.VISIBLE
                val stringColor2 = getPokemonColor(types[1].type)
                val colorId2 =
                    context.resources.getIdentifier(stringColor2, "color", context.packageName)
                binding.layoutInfoTypePokemon.type2.setChipBackgroundColorResource(colorId2)
                binding.layoutInfoTypePokemon.type2.text = types[1].type.name
            } else {
                binding.layoutInfoTypePokemon.type2.visibility = View.GONE
            }
        }
        else -> {
            binding.layoutInfoTypePokemon.type1.visibility = View.GONE
            binding.layoutInfoTypePokemon.type2.visibility = View.GONE
        }
    }

    @SuppressLint("MissingPermission")
    fun setupAlertDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Selecciona un elemento")
        builder.setNegativeButton("Cancelar", null)
        builder.setAdapter(listAdapter) { _, which ->
            val selectedData = dataList[which]
            val name = selectedData.name
            val macAddress = selectedData.address

            (context as DetailActivity).deviceSelected(macAddress)
        }
        alertDialog = builder.create()
    }

    @SuppressLint("MissingPermission")
    fun onSendClicked() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!androidPermissionChecker.check(Manifest.permission.BLUETOOTH_SCAN) ||
                    !androidPermissionChecker.check(Manifest.permission.ACCESS_FINE_LOCATION) ||
                !androidPermissionChecker.check(Manifest.permission.BLUETOOTH_CONNECT)) {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    PERMISSIONS_LOCATION,
                    1
                )
            }
        } else {
            if (!androidPermissionChecker.check(Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    PERMISSIONS_LOCATION,
                    1
                )
            }
        }
        val locationManager = context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!isGpsEnabled) {
            startActivityForResult(
                context as DetailActivity,
                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                1,
                null
            )
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (androidPermissionChecker.check(Manifest.permission.BLUETOOTH_SCAN) && isGpsEnabled) {
                if (!bluetoothAdapter.isEnabled) bluetoothAdapter.enable()
                if (bluetoothAdapter.isDiscovering) bluetoothAdapter.cancelDiscovery()
                bluetoothAdapter.startDiscovery()

                alertDialog.show()
                alertDialog.window?.setLayout(1000, 1600)
            }
        } else {
            if (androidPermissionChecker.check(Manifest.permission.ACCESS_FINE_LOCATION) && isGpsEnabled) {
                if (!bluetoothAdapter.isEnabled) bluetoothAdapter.enable()
                if (bluetoothAdapter.isDiscovering) bluetoothAdapter.cancelDiscovery()
                bluetoothAdapter.startDiscovery()

                alertDialog.show()
                alertDialog.window?.setLayout(1000, 1600)
            }
        }
    }

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("MissingPermission")
    fun updateListAdapter(list: List<BluetoothDevice>) {
        dataList.clear()
        dataList.addAll(list)
        listAdapter.clear()
        dataList.forEach { data ->
            val item = "${data.name} (${data.address})"
            listAdapter.add(item)
        }
    }

    fun showError(error: Error) = (when (error) {
        Error.Connectivity -> "Connection error"
        is Error.NullPointer -> "Null pointer error"
        is Error.Server -> "Server error"
        is Error.SocketTimeout -> "Connection timeout error"
        is Error.Unknown -> "Unknown error"
    }).apply {
        showDialog(context, this)
    }
}