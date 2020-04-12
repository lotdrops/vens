package hackovid.vens.common.utils

import android.content.Context

class FileReaderUtilities(private val context: Context) {

    fun readJsonFileFromAssets(filePath: String): String {
        return context.assets.open(filePath).bufferedReader().use { it.readText() }
    }
}
