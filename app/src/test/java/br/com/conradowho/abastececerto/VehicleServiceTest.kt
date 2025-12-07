package br.com.conradowho.abastececerto

import br.com.conradowho.abastececerto.entity.Vehicle
import br.com.conradowho.abastececerto.service.VehicleService
import org.junit.Assert
import org.junit.Test

class VehicleServiceTest {
    private val service = VehicleService() // Instancia a classe de serviço

    @Test
    fun getBestOption_withStandardRule_shouldReturnAlcohol() {
        //Arrange
        val gasPrice = 5.0
        val ethanolPrice = 3.45

        //Act
        val result = service.calculateBestFuel(gasPrice, ethanolPrice, null)
        Assert.assertEquals(FuelResult.ALCOHOL, result.fuelResult)
    }

    @Test
    fun getBestOption_withStandardRule_shouldReturnGasoline() {
        val result = service.calculateBestFuel(5.0, 4.00, null)
        Assert.assertEquals(FuelResult.GASOLINE, result.fuelResult)
    }

    @Test
    fun getBestOption_withVehicleConsumptionBetterInGasoline_shouldReturnGasoline() {
        //Arrange
        val efficientCar =
            Vehicle(1, "Fusca", 7.0, 14.0)

        //Act
        val result = service.calculateBestFuel(10.0, 10.0, efficientCar)


        //Assert
        Assert.assertEquals(0.7, result.gasPricePerKm, 0.1)
        Assert.assertEquals(1.4, result.ethanolPricePerKm, 0.1)
        Assert.assertEquals(FuelResult.GASOLINE, result.fuelResult)
    }

    @Test
    fun getBestOption_withVehicleConsumptionBetterInAlcohol_shouldReturnAlcohol() {
        //Arrange
               val efficientCar =
            Vehicle(1, "Fusca", 14.0, 7.0)

        //Act
        val result = service.calculateBestFuel(10.0, 10.0, efficientCar)

//Assert
        Assert.assertEquals(1.4, result.gasPricePerKm, 0.1)
        Assert.assertEquals(0.7, result.ethanolPricePerKm, 0.1)
        Assert.assertEquals(FuelResult.ALCOHOL, result.fuelResult)
    }
}