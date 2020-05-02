package hackovid.vens.common.utils

import android.widget.EditText

fun EditText.IsValidEmailField():Boolean {
    val mailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})";
    if(this.text.isNullOrEmpty()) return false
    return mailRegex.toRegex().matches(this.text)

}


fun EditText.passwordHaveLessThanSixCharacters():Boolean {
    if(this.text.isNullOrEmpty()) return false
    return this.text.length > 5
}
