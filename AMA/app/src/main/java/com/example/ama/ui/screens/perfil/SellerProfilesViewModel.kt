import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ama.core.dto.SellerProfileDto
import com.example.ama.data.network.NetworkModule
import com.example.ama.data.repository.SellerProfilesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SellerProfilesViewModel(
    private val repo: SellerProfilesRepository
) : ViewModel() {

    private val _profiles = MutableStateFlow<List<SellerProfileDto>>(emptyList())
    val profiles: StateFlow<List<SellerProfileDto>> = _profiles

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadSellerProfiles() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            runCatching { repo.getAll() }
                .onSuccess { _profiles.value = it }
                .onFailure { _error.value = it.message ?: "Error cargando artesanos" }
            _loading.value = false
        }
    }
}
class SellerProfilesViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Ajusta a tu forma real de construir ApiService/Repo:
        val api = NetworkModule.apiService
        val repo = SellerProfilesRepository(api)
        @Suppress("UNCHECKED_CAST")
        return SellerProfilesViewModel(repo) as T
    }
}
