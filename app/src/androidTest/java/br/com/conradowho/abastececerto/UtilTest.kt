package br.com.conradowho.abastececerto

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UtilTest {

    @Test
    fun testSetAndGetVehicleId() {
        // Assert
        val context = ApplicationProvider.getApplicationContext<Context>()
        val vehicleId = 123L

        // Act
        setVehicleIdInSharedPreferences(context, vehicleId)
        val retrievedVehicleId = getVehicleIdFromSharedPreferences(context)

        // Arrange
        assertEquals(vehicleId, retrievedVehicleId)
    }
}
