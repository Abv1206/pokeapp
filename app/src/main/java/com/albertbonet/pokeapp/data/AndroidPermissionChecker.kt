package com.albertbonet.pokeapp.data

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class AndroidPermissionChecker(private val context: Context): PermissionChecker {

    override fun check(permission: String): Boolean = ContextCompat.checkSelfPermission(
        context,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}