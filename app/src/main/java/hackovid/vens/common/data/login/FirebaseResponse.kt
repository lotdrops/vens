package hackovid.vens.common.data.login

import hackovid.vens.common.ui.UIState

data class FireBaseResponse(val success: Boolean = false, var error: UIState.Error? = null)

