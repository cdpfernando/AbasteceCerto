package br.com.conradowho.abastececerto

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.com.conradowho.abastececerto.MainActivity.Companion.EXTRA_VEHICLE_SELECTED
import br.com.conradowho.abastececerto.VehicleActivity.Companion.EXTRA_VEHICLE_EDIT
import br.com.conradowho.abastececerto.adapter.VehicleAdapter
import br.com.conradowho.abastececerto.databinding.ActivityVehicleListBinding
import br.com.conradowho.abastececerto.database.DatabaseHandler
import br.com.conradowho.abastececerto.entity.Vehicle


class VehicleListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVehicleListBinding
    private lateinit var dbHandler: DatabaseHandler
    private var currentVehicle: Vehicle? = null
     private lateinit var adapter: VehicleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVehicleListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets}

        dbHandler = DatabaseHandler.getInstance(this)

        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun setupListeners() {

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.btnAddFirst.setOnClickListener {
            openVehicleForm()
        }

        binding.ivAddHeader.setOnClickListener {
            openVehicleForm()
        }
    }

    private fun openVehicleForm() {
        val intent = Intent(this, VehicleActivity::class.java)
        startActivity(intent)
    }

    private fun loadData() {
        val vehicleList = dbHandler.getAll()
        updateListVisibilty(vehicleList)

        //todo: criar logica para destacar o veiculo selecionado
       adapter = VehicleAdapter(
            vehicleList = vehicleList,
            onSelect = { vehicle ->
                val resultIntent = Intent()
                resultIntent.putExtra(EXTRA_VEHICLE_SELECTED, vehicle)
                currentVehicle = vehicle
                setResult(RESULT_OK, resultIntent)
                finish()
            },
            onEdit = { vehicle ->
                val intent = Intent(this, VehicleActivity::class.java)
                intent.putExtra(EXTRA_VEHICLE_EDIT, vehicle)
                startActivity(intent)
            },
            onDelete = { vehicle ->
                confirmDelete(vehicle)
            }
        )

        binding.rvVehicles.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        binding.rvVehicles.adapter = adapter
    }

    private fun updateListVisibilty(vehicles: List<Vehicle>) {
        if (vehicles.isEmpty()) {
            binding.layoutEmptyState.visibility = View.VISIBLE
            binding.rvVehicles.visibility = View.GONE
            binding.ivAddHeader.visibility = View.GONE
        } else {
            binding.layoutEmptyState.visibility = View.GONE
            binding.rvVehicles.visibility = View.VISIBLE
            binding.ivAddHeader.visibility = View.VISIBLE
        }
    }

    //todo: remover strings hard coded
   private fun confirmDelete(vehicle: Vehicle) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Excluir Veículo")
            .setMessage("Tem certeza que deseja remover o veículo ${vehicle.name}?")
            .setPositiveButton("Excluir") { _, _ ->
                dbHandler.delete(vehicle._id)

                if(currentVehicle != null && currentVehicle!!._id == vehicle._id) {
                    currentVehicle = null

                    val resultIntent = Intent()
                    resultIntent.putExtra(EXTRA_VEHICLE_SELECTED, true)

                    setResult(RESULT_OK, resultIntent)
                }
                loadData()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
