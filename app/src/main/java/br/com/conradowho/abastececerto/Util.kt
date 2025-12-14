package br.com.conradowho.abastececerto

import android.content.Context
import androidx.annotation.StringRes

enum class FuelResult(@StringRes val displayNameResId: Int) {
    GASOLINE(R.string.fuel_gasoline),
    ALCOHOL(R.string.fuel_alcohol);
}

public fun setVehicleIdInSharedPreferences(context: Context, vehicleId: Long) {

    val PREFS_NAME = "ABASTECE_CERTO"
    val sp = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    sp.edit().putLong("VEHICLE_ID", vehicleId).commit()


}

public fun getVehicleIdFromSharedPreferences(context: Context): Long {
    var vehicleId: Long = 0;

    val PREFS_NAME = "ABASTECE_CERTO"

    val sp = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    vehicleId = sp.getLong("VEHICLE_ID", 0);
    return vehicleId;
}


