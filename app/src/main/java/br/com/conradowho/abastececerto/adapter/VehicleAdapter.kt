package br.com.conradowho.abastececerto.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.conradowho.abastececerto.R
import br.com.conradowho.abastececerto.databinding.ItemVehicleCardBinding
import br.com.conradowho.abastececerto.entity.Vehicle

class VehicleAdapter(
    private var vehicleList: List<Vehicle>,
    private val onSelect: (Vehicle) -> Unit,
    private val onEdit: (Vehicle) -> Unit,
    private val onDelete: (Vehicle) -> Unit
) : RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder>() {

    inner class VehicleViewHolder(val binding: ItemVehicleCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val binding =
            ItemVehicleCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VehicleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        val vehicle = vehicleList[position]

        holder.binding.tvVehicleName.text = vehicle.name
        holder.binding.tvVehicleStatus.text = holder.itemView.context.getString(
            R.string.consumption_full,
            vehicle.alcoholConsumption,
            vehicle.gasolineConsumption
        )

        holder.itemView.setOnClickListener {
            onSelect(vehicle)
        }

        holder.binding.ivEdit.setOnClickListener {
            onEdit(vehicle)
        }

        holder.binding.ivAction.setOnClickListener {
            onDelete(vehicle)
        }
    }

    override fun getItemCount() = vehicleList.size
}
