package com.oceantech.monitorsaude.viewmodel

import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.oceantech.monitorsaude.MedicamentoRepository
import com.oceantech.monitorsaude.MyApplication
import com.oceantech.monitorsaude.model.Medicamento
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class MedicamentoViewModel(private val medicamentoRepository: MedicamentoRepository, private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _medicamentos = MutableLiveData<List<Medicamento>>()
    val medicamentos: LiveData<List<Medicamento>> = _medicamentos


    private val _datasSelecionadas = MutableLiveData<List<String>>()
    val datasSelecionadas: LiveData<List<String>> = _datasSelecionadas

    private val _horariosSelecionados = MutableLiveData<List<String>>()
    val horariosSelecionados: LiveData<List<String>> = _horariosSelecionados

    fun resetMedicamento() {
        // Resete todas as propriedades para o valor inicial
        _medicamentos.value = emptyList()

        // Chame o método para limpar as listas de datas e horários
        limparListas()
    }

    fun inserirMedicamento(medicamento: Medicamento) {
        viewModelScope.launch {
            medicamentoRepository.insert(medicamento.copy(datas = datasSelecionadas.value ?: emptyList(), horarios = horariosSelecionados.value ?: emptyList()))
        }
        limparListas()
    }

    // Limpa as listas de datas e horários
    private fun limparListas() {
        _datasSelecionadas.value = emptyList()
        _horariosSelecionados.value = emptyList()
    }

    fun onHorarioSelecionado(timePicker: String) {
        val horario = "${timePicker}"
        addHorario(horario)
    }

    fun onDataSelecionada(date: Date) {
        val dataFormatada = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
        addData(dataFormatada)
    }

    fun loadMedications() {
        viewModelScope.launch {
            _medicamentos.value = medicamentoRepository.getAll()
        }
    }

    private fun addData(data: String) {
        _datasSelecionadas.value = (_datasSelecionadas.value ?: emptyList()) + listOf(data)
    }

    private fun addHorario(horario: String) {
        _horariosSelecionados.value = (_horariosSelecionados.value ?: emptyList()) + listOf(horario)
    }

    suspend fun getById(id: Int): Medicamento? = medicamentoRepository.getById(id)

    fun deleteMedication(medicamento: Medicamento) {
        viewModelScope.launch {
            medicamentoRepository.delete(medicamento)
        }
    }

    // Define ViewModel factory in a companion object
    companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[APPLICATION_KEY])
                // Create a SavedStateHandle for this ViewModel from extras
                val savedStateHandle = extras.createSavedStateHandle()

                return MedicamentoViewModel(
                    (application as MyApplication).medicamentoRepository,
                    savedStateHandle
                ) as T
            }
        }
    }
}


