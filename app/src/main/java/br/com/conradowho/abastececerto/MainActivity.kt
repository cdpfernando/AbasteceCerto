package br.com.conradowho.abastececerto

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.com.conradowho.abastececerto.ResultActivity.Companion.EXTRA_CALCULATION_RESULT
import br.com.conradowho.abastececerto.database.DatabaseHandler
import br.com.conradowho.abastececerto.databinding.ActivityMainBinding
import br.com.conradowho.abastececerto.entity.Vehicle
import br.com.conradowho.abastececerto.service.VehicleService

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentVehicle: Vehicle? = null
    private var service: VehicleService = VehicleService()

    companion object {
        val EXTRA_VEHICLE_DELETED = "EXTRA_VEHICLE_DELETED"
        val EXTRA_VEHICLE_SELECTED = "EXTRA_VEHICLE_SELECTED"

    }

    private lateinit var db: DatabaseHandler
    private val vehicleActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val wasDeleted = data?.getBooleanExtra(EXTRA_VEHICLE_DELETED, false) ?: false
            if (wasDeleted) {
                currentVehicle = null
                updateVehicleDisplay(null)
                return@registerForActivityResult
            }
            currentVehicle =
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    data?.getParcelableExtra(EXTRA_VEHICLE_SELECTED, Vehicle::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    (data?.getParcelableExtra(EXTRA_VEHICLE_SELECTED))
                }

            updateVehicleDisplay(currentVehicle)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()

        db = DatabaseHandler.getInstance(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupListeners()
        updateVehicleDisplay(currentVehicle)

    }

    override fun onResume() {
        super.onResume()

        updateCurrentVehicle()
    }

    private fun updateCurrentVehicle() {
        if (currentVehicle != null) {
            val existentVehicle = db.getById(currentVehicle!!._id)
            if (existentVehicle == null) {
                currentVehicle = null
                updateVehicleDisplay(null)
            } else {
                currentVehicle = existentVehicle
                updateVehicleDisplay(existentVehicle)
            }
        }
    }

    private fun setupListeners() {
        binding.containerEmptyVehicle.setOnClickListener {
            runVehicleListActivity(null)
        }

        binding.cardVehicle.setOnClickListener {
            runVehicleListActivity(currentVehicle)
        }

        binding.btnCalculate.setOnClickListener {

            if (binding.etGasoline.text.isNullOrBlank() || binding.etAlcohol.text.isNullOrBlank()) {
                return@setOnClickListener

            }

            val result = service.getBestOption(
                binding.etGasoline.text.toString().toDouble(),
                binding.etAlcohol.text.toString().toDouble(),
                currentVehicle
            )

            val intent = Intent(this, ResultActivity::class.java)

            intent.putExtra(EXTRA_CALCULATION_RESULT, result)

            startActivity(intent)

        }
    }

    private fun updateVehicleDisplay(vehicle: Vehicle?) {

        if (vehicle != null) {
            binding.containerEmptyVehicle.visibility = View.GONE
            binding.cardVehicle.visibility = View.VISIBLE

            binding.tvVehicleName.text = vehicle.name
            binding.tvVehicleConsumption.text = getString(
                R.string.consumption_full, vehicle.alcoholConsumption, vehicle.gasolineConsumption
            )
        } else {
            binding.containerEmptyVehicle.visibility = View.VISIBLE
            binding.cardVehicle.visibility = View.GONE
        }
    }

    private fun runVehicleListActivity(vehicle: Vehicle?) {
        val intent = Intent(this, VehicleListActivity::class.java)

        //todo: criar logica para enviar o veiculo selecionado

        vehicleActivityLauncher.launch(intent)
    }
}