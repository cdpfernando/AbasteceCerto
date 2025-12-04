package br.com.conradowho.abastececerto.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Vehicle(
    val _id: Long,
    val name: String,
    val alcoholConsumption: Double,
    val gasolineConsumption: Double
) : Parcelable{
    override fun toString(): String {
        return name
    }
    //todo: remover strings hard coded
    fun getAbbreviatedConsumptionDescription() : String {

        return "A: ${this.alcoholConsumption} / G: ${this.gasolineConsumption}"
    }
    fun getFullConsumptionDescription() : String {

        return "Alcool: ${this.alcoholConsumption} / Gasolina: ${this.gasolineConsumption}"
    }
}