package com.oceantech.monitorsaude

import com.oceantech.monitorsaude.viewmodel.MedicamentoViewModel
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.oceantech.monitorsaude.databinding.ActivityMedicamentoBinding
import com.oceantech.monitorsaude.extensions.formatDate
import com.oceantech.monitorsaude.extensions.formatTime
import com.oceantech.monitorsaude.extensions.text
import com.oceantech.monitorsaude.extensions.toDate
import com.oceantech.monitorsaude.model.Medicamento
import kotlinx.coroutines.launch
import java.util.*


class MedicamentoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedicamentoBinding

    private val viewModel: MedicamentoViewModel by viewModels {
        MedicamentoViewModel.Factory
    }

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
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .build()

            datePicker.addOnPositiveButtonClickListener { timestamp ->
                viewModel.onDataSelecionada(Date(timestamp))
                binding.txtData.text = Date(timestamp).formatDate()
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }

        binding.txtHour.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()
            timePicker.addOnPositiveButtonClickListener {
                val hour = if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour.toString()
                val minute = if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute.toString()
                val horario = "$hour:$minute"
                viewModel.onHorarioSelecionado(horario)
                binding.txtHour.text = horario
            }
            timePicker.show(supportFragmentManager, null)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Tratar evento de clique no botão de voltar
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
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

        val medicamento = Medicamento(
            medicationId,
            nome = nome,
            dosagem = dosagem,
            horarios = horas.split(","),
            datas = datas.split(",")
        )

        val datasFormatadas = medicamento.datas.map { it.toDate("dd/MM/yyyy") }
            .map { it.formatDate() }

        val horariosFormatados = medicamento.horarios.map { it.toDate("HH:mm") }
            .map { it.formatTime() }

        val medicamentoAtualizado = medicamento.copy(datas = datasFormatadas, horarios = horariosFormatados)

        viewModel.inserirMedicamento(medicamentoAtualizado)

        finish()
    }

    companion object{
        const val MEDICAMENTO_ID = "medicamento_id"
    }
}

