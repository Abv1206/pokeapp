package com.albertbonet.pokeapp.ui.detail

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.Observer
import com.albertbonet.pokeapp.data.AndroidPermissionChecker
import com.albertbonet.pokeapp.data.datasource.IPokemonBluetoothDataSource
import com.albertbonet.pokeapp.databinding.ActivityDetailBinding
import com.albertbonet.pokeapp.ui.common.launchAndCollect
import com.albertbonet.pokeapp.ui.common.loadUrl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject


@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    companion object {
        const val POKEMON = "DetailActivity:pokemon"
    }

    private val androidPermissionChecker = AndroidPermissionChecker(this)

    @Inject lateinit var bluetoothPokemon: IPokemonBluetoothDataSource
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private val viewModel: DetailViewModel by viewModels()
    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailState: DetailState

    private var mutableSet: MutableSet<BluetoothDevice> = HashSet()


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pokemonDetailToolbar.setNavigationOnClickListener { onBackPressed() }

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        detailState = DetailState(this, bluetoothAdapter)
        detailState.setupAlertDialog()

        with(viewModel.state) {
            diff({ it.pokemon?.name }) { binding.toolbarTitle.text = it }
            diff({ it.pokemon }) { detailState.setPokemonInfo(binding, it) }
            diff({ it.pokemon?.sprites?.frontDefaultUrl }) {
                it?.let { binding.pokemonArtImage.loadUrl(it) }
            }
            diff({ it.error }) { it?.let { detailState.showError(it) } }
            diff({ it.pokemon?.types }) {
                detailState.setDetailBackground(binding.backgroundImageView, it)
                detailState.setChipsType(binding, it)
            }
        }

        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
        filter.addAction(BluetoothConnectionReceiver.CONNECTION_PAIRED)
        filter.addAction(BluetoothConnectionReceiver.POKEMON_SENT)
        registerReceiver(receiver, filter)
        viewModel.btLiveData.observe(this, Observer { list ->
            detailState.updateListAdapter(list)
        })

        binding.send.setOnClickListener {
            detailState.onSendClicked()
            return@setOnClickListener
        }
    }

    fun deviceSelected(macAddress: String) {
        viewModel.onRequestBluetoothConnection(macAddress)
    }

    private val receiver = object : BroadcastReceiver() {

        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            when(intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    device?.let {
                        if (device.name != null) {
                            mutableSet.add(it)
                            viewModel.btLiveData.value = mutableSet.toList()
                        }
                    }
                }
                BluetoothConnectionReceiver.CONNECTION_PAIRED -> {
                    viewModel.onSendPokemon()
                }
                BluetoothConnectionReceiver.POKEMON_SENT -> {
                    detailState.showToast("Pokemon sent successfully")
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                println("OnActivityResult - Location Settings - OK")
            } else {
                detailState.showToast("Enable location settings and set it as high precision")
                println("OnActivityResult - Location Settings - KO")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.onUiReady()
    }

    @SuppressLint("MissingPermission")
    override fun onDestroy() {
        super.onDestroy()
        if (androidPermissionChecker.check(Manifest.permission.BLUETOOTH_SCAN)) {
            if (bluetoothAdapter.isDiscovering) bluetoothAdapter.cancelDiscovery()
        }
        unregisterReceiver(receiver)
    }

    private fun <T, U> Flow<T>.diff(mapf: (T) -> U, body: (U) -> Unit) {
        launchAndCollect(
            flow = map(mapf).distinctUntilChanged(),
            body = body
        )
    }
}