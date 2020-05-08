package hackovid.vens.features.register

class RegisterFieldsValidator {
    fun isValidEmail(email: String?) = email != null && MAIL_REGEX.toRegex().matches(email)

    fun isValidName(name: String?) = name != null && name.trim().length >= MIN_NAME_LENGTH

    fun isValidAddress(address: String?) =
        address != null && address.trim().length >= MIN_ADDRESS_LENGTH

    fun isValidPassword(password: String?) =
        password != null && password.trim().length >= MIN_PWD_LENGTH

    companion object {
        const val MIN_NAME_LENGTH = 2
        const val MIN_ADDRESS_LENGTH = 5
        const val MIN_PWD_LENGTH = 6
    }
}
private const val MAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
