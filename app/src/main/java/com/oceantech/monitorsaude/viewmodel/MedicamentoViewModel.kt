package com.oceantech.monitorsaude.viewmodel

import androidx.lifecycle.*
import com.oceantech.monitorsaude.MedicamentoRepository
import com.oceantech.monitorsaude.model.Medicamento
import kotlinx.coroutines.launch


class MedicamentoViewModel(private val medicamentoRepository: MedicamentoRepository) : ViewModel() {

    private val _medicamentos = MutableLiveData<List<Medicamento>>()
    val medicamentos: LiveData<List<Medicamento>> = _medicamentos


    var datas: MutableList<String> = mutableListOf()

    var horarios: MutableList<String> = mutableListOf()

    // Adiciona a data selecionada à lista de datas
    private fun addData(data: String) {
        datas.add(data)
    }

    // Adiciona a hora selecionada à lista de horários
    private fun addHorario(horario: String) {
        horarios.add(horario)
    }

    // Limpa as listas de datas e horários
    private fun limparListas() {
        datas.clear()
        horarios.clear()
    }

    // Restaura as listas de datas e horários para seus valores iniciais
    fun resetListas() {
        limparListas()
    }

    // Restaura todas as alterações não salvas nas listas de datas e horários
    fun cancelarAlteracoes() {
        resetListas()
    }
    // Listener para seleção de data
    val onDataSelecionada = { data: String ->
        addData(data)
    }

    // Listener para seleção de hora
    val onHorarioSelecionado = { horario: String ->
        addHorario(horario)
    }
    fun resetMedicamento() {
        horarios.clear()
        datas.clear()
    }

    fun loadMedications() {
        viewModelScope.launch {
            _medicamentos.value = medicamentoRepository.getAll()
        }
    }

    // Insere um novo medicamento no banco de dados com as listas de datas e horários atualizadas
    fun inserirMedicamento(medicamento: Medicamento) {
        viewModelScope.launch {
            medicamentoRepository.insert(medicamento)
        }
        limparListas()
    }

    suspend fun getById(id: Int): Medicamento? = medicamentoRepository.getById(id)

    fun deleteMedication(medicamento: Medicamento) {
        viewModelScope.launch {
            medicamentoRepository.delete(medicamento)
        }
    }
}

