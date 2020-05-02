package hackovid.vens.common.ui


sealed class UIState {
    object Loading: UIState()
    data class Error( val errorMessage: Int): UIState()
    object Success: UIState()
}