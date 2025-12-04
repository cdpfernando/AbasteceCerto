package br.com.conradowho.abastececerto.service

import br.com.conradowho.abastececerto.entity.CalculationResult
import br.com.conradowho.abastececerto.FuelResult
import br.com.conradowho.abastececerto.entity.Vehicle

class VehicleService {

    fun getBestOption(gasPrice: Double, ethanolPrice: Double, vehicleItem: Vehicle?): CalculationResult {

        var costKmGasoline = 0.0
        var costKmEthanol = 0.0

        var fuelResult = FuelResult.GASOLINE
        if (vehicleItem != null) {

            costKmEthanol = ethanolPrice / vehicleItem.alcoholConsumption
            costKmGasoline = gasPrice / vehicleItem.gasolineConsumption

            if (costKmEthanol < costKmGasoline) {
                fuelResult = FuelResult.ALCOHOL
            } else {
                fuelResult = FuelResult.GASOLINE
            }
        }

        val ratio = ethanolPrice / gasPrice

        if (ratio < 0.7) {
            fuelResult = FuelResult.ALCOHOL    } else {
            fuelResult = FuelResult.GASOLINE
        }

        return  CalculationResult(fuelResult, gasPrice, ethanolPrice, vehicleItem,costKmGasoline,costKmEthanol)
    }
}