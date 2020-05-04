package hackovid.vens.common.ui

sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    data class Error(val errorMessage: Int) : UiState()
    object Success : UiState()
}
