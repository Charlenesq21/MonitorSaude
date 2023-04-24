package com.oceantech.monitorsaude.viewmodel

import androidx.lifecycle.*
import com.oceantech.monitorsaude.MedicamentoRepository
import com.oceantech.monitorsaude.model.Medicamento
import kotlinx.coroutines.launch


class MedicamentoViewModel(private val medicamentoRepository: MedicamentoRepository, private val lifecycleOwner: LifecycleOwner) : ViewModel() {

    private val _medicamentos = MutableLiveData<List<Medicamento>>()
    val medicamentos: LiveData<List<Medicamento>> = _medicamentos

    // Adicione a propriedade selectedTimes
    var selectedTimes: MutableList<String> = mutableListOf()

    // Adicione uma propriedade para armazenar a lista de horários selecionados
    var horarios: MutableList<String> = mutableListOf()

    fun loadMedications() {
        viewModelScope.launch {
            _medicamentos.value = medicamentoRepository.getAll()
        }
    }

    fun insertMedication(medicamento: Medicamento) {
        viewModelScope.launch {
            medicamentoRepository.insert(medicamento)
        }
    }


    suspend fun getById(id: Int): Medicamento? = medicamentoRepository.getById(id)


    fun deleteMedication(medicamento: Medicamento) {
        viewModelScope.launch {
            medicamentoRepository.delete(medicamento)
        }
    }

    // Adicione uma função para atualizar a lista de horários selecionados
    fun updateSelectedTimes(selectedTimes: List<String>) {
        this.selectedTimes.clear()
        this.selectedTimes.addAll(selectedTimes)
    }

    // Adicione a função para atualizar a lista de horários selecionados
    fun updateHorarios(horarios: List<String>) {
        this.horarios.clear()
        this.horarios.addAll(horarios)
    }

    // Adicione o método para resetar os valores do medicamento
    fun resetMedicamento() {
        selectedTimes.clear()
        horarios.clear()
    }

}

