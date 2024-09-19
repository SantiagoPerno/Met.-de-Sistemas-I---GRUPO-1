package utn.methodology.domain.entities.entity

data class User (
    val uuid: Int = generateUuid(),
    val name: String,
    val username: String,
    var email: String,
    var password: String
) {
    companion object {
        private var lastUuid: Int = 0

        private fun generateUuid(): Int {
            lastUuid += 1
            return lastUuid

        }
    }
}