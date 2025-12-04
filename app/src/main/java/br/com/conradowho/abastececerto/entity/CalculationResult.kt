package br.com.conradowho.abastececerto.entity
import android.os.Parcelable
import br.com.conradowho.abastececerto.FuelResult
import kotlinx.parcelize.Parcelize
@Parcelize
data class CalculationResult(
    val fuelResult: FuelResult,
    val gasPrice: Double,
    val ethanolPrice: Double,
    val vehicleItem: Vehicle?,
    val gasPricePerKm : Double,
    val ethanolPricePerKm : Double) : Parcelable{
}

