package br.com.conradowho.abastececerto
import androidx.annotation.StringRes

enum class FuelResult(@StringRes val displayNameResId: Int) {
    GASOLINE(R.string.fuel_gasoline),
    ALCOHOL(R.string.fuel_alcohol);
}


