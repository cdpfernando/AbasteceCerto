package br.com.conradowho.abastececerto.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Vehicle(
    val _id: Long,
    val name: String,
    val alcoholConsumption: Double,
    val gasolineConsumption: Double
) : Parcelable {
    override fun toString(): String {
        return name
    }

}