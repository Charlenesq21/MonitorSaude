package com.oceantech.monitorsaude

import MedicamentoViewModel
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.oceantech.monitorsaude.databinding.ActivityMedicamentoBinding


class MedicamentoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedicamentoBinding
    private val viewModel: MedicamentoViewModel by viewModels()

    private var medicationId = 0

    override suspend fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedicamentoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura a Toolbar
        val toolbar: MaterialToolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent.hasExtra(MEDICAMENTO_ID)) {
            medicationId = intent.getIntExtra(MEDICAMENTO_ID, 0)
            viewModel.getById(medicationId)
        }

        viewModel.medicamentos.observe(this, { medicamento ->
            medicamento?.let {
                binding.tilName.text = it.name
                binding.tilDosage.text = it.dosage
                binding.tilTimes.text = it.times
                binding.tilDaysOfWeek.text = it.daysOfWeek
            }
        })

        insertListeners()
    }

    private fun insertListeners() {
        binding.tilTimes.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()
            timePicker.addOnPositiveButtonClickListener {
                val minute = if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                val hour = if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour
                viewModel.times.value = "$hour:$minute"
            }
            timePicker.show(supportFragmentManager, null)
        }

        binding.tilDaysOfWeek.editText?.setOnClickListener {
            val daysOfWeek = resources.getStringArray(R.array.days_of_week)

            val selectedItems = BooleanArray(daysOfWeek.size) { false }
            val selectedDaysOfWeek = binding.tilDaysOfWeek.text.split(", ").filter { it.isNotBlank() }.toTypedArray()

            for (i in daysOfWeek.indices) {
                if (daysOfWeek[i] in selectedDaysOfWeek) {
                    selectedItems[i] = true
                }
            }

            MaterialAlertDialogBuilder(this)
                .setTitle("Select days of week")
                .setMultiChoiceItems(daysOfWeek, selectedItems) { dialog, which, isChecked ->
                    selectedItems[which] = isChecked
                }
                .setPositiveButton("OK") { dialog, which ->
                    val selectedDays = daysOfWeek.filterIndexed { index, s ->
                        selectedItems[index]
                    }.joinToString(", ")
                    viewModel.daysOfWeek.value = selectedDays
                }
                .show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!isChangingConfigurations) {
            viewModel.resetMedication()
        }
    }

    private fun saveMedication() {
        val name = binding.tilName.text
        val dosage = binding.tilDosage.text
        val times = binding.tilTimes.text
        val daysOfWeek = binding.tilDaysOfWeek.text

        if (name.isNullOrBlank() || dosage.isNullOrBlank() || times.isNullOrBlank() || daysOfWeek.isNullOrBlank()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.saveMedication(
            Medication(
                medicationId,
                name.toString(),
                dosage.toString(),
                times.toString(),
                daysOfWeek.toString()
            )
        )

        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_medication, menu)
        return super.onCreateOptionsMenu(menu)
    }

    companion object{
        const val MEDICAMENTO_ID = "task_id"
    }
}

