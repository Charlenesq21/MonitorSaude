import androidx.lifecycle.*
import com.oceantech.monitorsaude.MedicamentoRepository
import com.oceantech.monitorsaude.model.Medicamento
import kotlinx.coroutines.launch

class MedicamentoViewModel(private val medicamentoRepository: MedicamentoRepository, private val lifecycleOwner: LifecycleOwner
) : ViewModel() {

    private val _medicamentos = MutableLiveData<List<Medicamento>>()
    val medicamentos: LiveData<List<Medicamento>> = _medicamentos

    fun loadMedications() {
        lifecycleOwner.lifecycleScope.launch {
            _medicamentos.value = medicamentoRepository.getAll()
        }
    }

    fun insertMedication(medicamento: Medicamento) {
        lifecycleOwner.lifecycleScope.launch {
            medicamentoRepository.insert(medicamento)
        }
    }

    suspend fun getById(id: Int): Medicamento? = medicamentoRepository.getById(id)


    fun deleteMedication(medicamento: Medicamento) {
        lifecycleOwner.lifecycleScope.launch {
            medicamentoRepository.delete(medicamento)
        }
    }
}
