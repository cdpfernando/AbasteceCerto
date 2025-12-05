package br.com.conradowho.abastececerto

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.com.conradowho.abastececerto.databinding.ActivityResultBinding
import br.com.conradowho.abastececerto.entity.CalculationResult

class ResultActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_CALCULATION_RESULT = "EXTRA_CALCULATION_RESULT"
    }

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val result =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(EXTRA_CALCULATION_RESULT, CalculationResult::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra(EXTRA_CALCULATION_RESULT) as? CalculationResult
            }

        if (result != null) {
            if (result.vehicleItem != null) {
                binding.tvGasInfo.text = getString(
                    R.string.fuel_info_format,
                    result.gasPrice,
                    result.vehicleItem.gasolineConsumption
                )
                binding.tvAlcoholInfo.text = getString(
                    R.string.fuel_info_format,
                    result.ethanolPrice,
                    result.vehicleItem.alcoholConsumption
                )
                binding.tvGasCostKm.text =
                    getString(R.string.fuel_price_format, result.gasPricePerKm)
                binding.tvAlcoholCostKm.text =
                    getString(R.string.fuel_price_format, result.ethanolPricePerKm)
            } else {
                binding.tvGasInfo.text =
                    getString(R.string.fuel_price_liter_format, result.gasPrice)
                binding.tvAlcoholInfo.text =
                    getString(R.string.fuel_price_liter_format, result.ethanolPrice)

                binding.tvGasCostKm.text = getString(R.string.empty_value)
                binding.tvAlcoholCostKm.text = getString(R.string.empty_value)
            }

            setupWinner(result.fuelResult)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupListeners()
    }

    private fun setupWinner(bestOption: FuelResult?) {
        val colorPrimary = getThemeColor(this, androidx.appcompat.R.attr.colorPrimary)

        when (bestOption) {
            FuelResult.GASOLINE -> {
                setupGasolineOption()

            }

            FuelResult.ALCOHOL -> {
                setupAlcoholWiner()
            }

            else -> setupGasolineOption()

        }

        binding.tvWinnerName.text = getString(bestOption!!.displayNameResId)
        binding.tvWinnerName.setTextColor(colorPrimary)


    }

    private fun setupAlcoholWiner() {
        applyWinnerStyle(
            card = binding.cardResumoAlcool,
            priceText = binding.tvAlcoholCostKm,
            titleText = binding.tvAlcoholName,
            infoText = binding.tvAlcoholInfo,
            iconContainer = binding.flIconAlc,
            iconImage = binding.flIconAlc.getChildAt(0) as ImageView
        )
        applyLoserStyle(
            card = binding.cardResumoGasolina,
            priceText = binding.tvGasCostKm,
            titleText = binding.tvGasName,
            infoText = binding.tvGasInfo,
            iconContainer = binding.flIconGas,
            iconImage = binding.flIconGas.getChildAt(0) as ImageView
        )
    }

    private fun setupGasolineOption() {
        applyWinnerStyle(
            card = binding.cardResumoGasolina,
            priceText = binding.tvGasCostKm,
            titleText = binding.tvGasName,
            infoText = binding.tvGasInfo,
            iconContainer = binding.flIconGas,
            iconImage = binding.flIconGas.getChildAt(0) as ImageView
        )
        applyLoserStyle(
            card = binding.cardResumoAlcool,
            priceText = binding.tvAlcoholCostKm,
            titleText = binding.tvAlcoholName,
            infoText = binding.tvAlcoholInfo,
            iconContainer = binding.flIconAlc,
            iconImage = binding.flIconAlc.getChildAt(0) as ImageView
        )
    }

    private fun applyWinnerStyle(
        card: com.google.android.material.card.MaterialCardView,
        priceText: TextView,
        titleText: TextView,
        infoText: TextView,
        iconContainer: android.view.View,
        iconImage: ImageView
    ) {
        val colorPrimary = getThemeColor(this, androidx.appcompat.R.attr.colorPrimary)

        val colorPrimaryContainer =
            getThemeColor(this, com.google.android.material.R.attr.colorPrimaryContainer)
        val colorOnPrimaryContainer =
            getThemeColor(this, com.google.android.material.R.attr.colorOnPrimaryContainer)
        val colorOnSurfaceVariant =
            getThemeColor(this, com.google.android.material.R.attr.colorOnSurfaceVariant)

        card.strokeColor = colorPrimary
        card.strokeWidth = 6

        titleText.setTextColor(colorPrimary)
        infoText.setTextColor(colorOnSurfaceVariant)
        priceText.setTextColor(colorPrimary)
        iconContainer.background.setTint(colorPrimaryContainer)
        iconImage.setColorFilter(colorOnPrimaryContainer)
    }

    private fun applyLoserStyle(
        card: com.google.android.material.card.MaterialCardView,
        priceText: TextView,
        titleText: TextView,
        infoText: TextView,
        iconContainer: android.view.View,
        iconImage: ImageView
    ) {
        val colorOutline =
            getThemeColor(this, com.google.android.material.R.attr.colorOutlineVariant)
        val colorOnSurface = getThemeColor(this, com.google.android.material.R.attr.colorOnSurface)
        val colorOnSurfaceVariant =
            getThemeColor(this, com.google.android.material.R.attr.colorOnSurfaceVariant)
        val colorSurfaceVariant =
            getThemeColor(this, com.google.android.material.R.attr.colorSurfaceVariant)

        card.strokeColor = colorOutline
        card.strokeWidth = 2

        titleText.setTextColor(colorOnSurface)
        priceText.setTextColor(colorOnSurfaceVariant)
        infoText.setTextColor(colorOnSurfaceVariant)

        iconContainer.background.setTint(colorSurfaceVariant)
        iconImage.setColorFilter(colorOnSurfaceVariant)
    }

    private fun getThemeColor(context: android.content.Context, attrId: Int): Int {
        val typedValue = android.util.TypedValue()
        context.theme.resolveAttribute(attrId, typedValue, true)
        return typedValue.data
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.btnRecalculate.setOnClickListener {
            finish()
        }
    }
}