package utn.methodology.domain.entities.entity

data class User(
    val uuid: Int = generarUuid(),
    val nombre: String,
    val username: String,
    var email: String,
    var contrasenia: String
) {
    companion object {
        private var ultimoUuid: Int = 0

        private fun generarUuid(): Int {
            ultimoUuid += 1
            return ultimoUuid
        }
    }
}