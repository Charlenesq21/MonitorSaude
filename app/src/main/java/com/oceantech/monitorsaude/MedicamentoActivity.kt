package com.oceantech.monitorsaude

import com.oceantech.monitorsaude.viewmodel.MedicamentoViewModel
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.oceantech.monitorsaude.databinding.ActivityMedicamentoBinding
import com.oceantech.monitorsaude.extensions.format
import com.oceantech.monitorsaude.extensions.text
import com.oceantech.monitorsaude.model.Medicamento
import kotlinx.coroutines.launch
import java.util.*


class MedicamentoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedicamentoBinding
    private val viewModel: MedicamentoViewModel by viewModels()

    private var medicationId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedicamentoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura a Toolbar
        val toolbar: MaterialToolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent.hasExtra(MEDICAMENTO_ID)) {
            medicationId = intent.getIntExtra(MEDICAMENTO_ID, 0)
            lifecycleScope.launch{
                viewModel.getById(medicationId)
            }

        }

        insertListeners()
    }

    private fun insertListeners() {

        binding.txtData.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()

            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                binding.txtData.text = Date(it + offset).format()
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }

        binding.txtHour.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()
            timePicker.addOnPositiveButtonClickListener {
                binding.txtHour.text = "${timePicker.hour} ${timePicker.minute}"
            }

            timePicker.show(supportFragmentManager, null)
        }

        binding.txtHour.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()
            timePicker.addOnPositiveButtonClickListener {
                val minute = if(timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                val hour = if(timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour
                binding.txtHour.text = "$hour:$minute"
            }
            timePicker.show(supportFragmentManager,null)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!isChangingConfigurations) {
            viewModel.resetMedicamento()
        }
    }

    private fun saveMedication() {
        var nome = binding.txtNome.editText?.text.toString()
        val dosagem = binding.txtDosagem.editText?.text.toString()
        val horas = binding.txtHour.editText?.text.toString()
        val datas = binding.txtData.editText?.text.toString()

        if (nome.isNullOrBlank() || dosagem.isNullOrBlank() || horas.isNullOrBlank() || datas.isNullOrBlank()) {
            Toast.makeText(this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.insertMedication(
            Medicamento(
                medicationId,
                nome = nome,
                dosagem = dosagem,
                horarios = horas.split(","),
                datas = datas.split(",")
            )
        )

        finish()
    }

    companion object{
        const val MEDICAMENTO_ID = "medicamento_id"
    }
}

