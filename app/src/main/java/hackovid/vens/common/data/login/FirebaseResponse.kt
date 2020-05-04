package hackovid.vens.common.data.login

import hackovid.vens.common.ui.UiState

data class FirebaseResponse(val success: Boolean = false, var error: UiState.Error? = null)
