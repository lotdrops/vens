package hackovid.vens.common.utils

import android.content.Context

sealed class UiTextOrResource {
    abstract fun getText(context: Context): String
}

data class UiText(val text: String) : UiTextOrResource() {
    override fun getText(context: Context) = text
}

data class ResourceString(val resource: Int) : UiTextOrResource() {
    override fun getText(context: Context) = context.resources.getString(resource)
}

fun String.toUiText() = UiText(this)