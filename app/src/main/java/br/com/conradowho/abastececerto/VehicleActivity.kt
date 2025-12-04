package br.com.conradowho.abastececerto

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.com.conradowho.abastececerto.database.DatabaseHandler
import br.com.conradowho.abastececerto.databinding.ActivityVehicleBinding
import br.com.conradowho.abastececerto.entity.Vehicle

class VehicleActivity : AppCompatActivity() {
    companion object {
        val EXTRA_VEHICLE_EDIT = "EXTRA_VEHICLE_EDIT"
    }
    private lateinit var binding: ActivityVehicleBinding
    private var currentVehicle: Vehicle? = null
    private lateinit var db: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityVehicleBinding.inflate(layoutInflater)

        setContentView(binding.root)
        db = DatabaseHandler.getInstance(this)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val vehicle =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(EXTRA_VEHICLE_EDIT, Vehicle::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra(EXTRA_VEHICLE_EDIT) as? Vehicle
            }

        fillVehicleFields(vehicle)

        setupListeners()

        binding.main.requestFocus()

    }

    private fun fillVehicleFields(vehicle: Vehicle?) {
        if (vehicle == null)
            return
        this.currentVehicle = vehicle
        binding.etVehicleName.setText(vehicle.name)
        binding.etAlcoholKm.setText(vehicle.alcoholConsumption.toString())
        binding.etGasolineKm.setText(vehicle.gasolineConsumption.toString())
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener { finish() }
        binding.btnCancel.setOnClickListener { finish() }

        binding.btnSave.setOnClickListener {
            val name = binding.etVehicleName.text.toString()
            val alcohol = binding.etAlcoholKm.text.toString().toDoubleOrNull() ?: 0.0
            val gasoline = binding.etGasolineKm.text.toString().toDoubleOrNull() ?: 0.0

            val vehicle = Vehicle(
                _id = currentVehicle?._id ?: 0,
                name = name,
                alcoholConsumption = alcohol,
                gasolineConsumption = gasoline
            )

            if (vehicle._id > 0)
                db.update(vehicle)
            else
                db.insert(vehicle)

            finish()
        }

    }


}