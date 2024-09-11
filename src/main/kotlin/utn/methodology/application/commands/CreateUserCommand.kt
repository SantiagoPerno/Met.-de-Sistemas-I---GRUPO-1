package utn.methodology.application.commands

import kotlinx.serialization.Serializable

@Serializable
class CreateUserCommand(
    val uuid: Int,
    val name: String,
    val userName: String,
    val email: String,
    val password: String
) {
    fun validate(): CreateUserCommand {
        checkNotNull(uuid) { throw IllegalArgumentException("UUID must be defined") }
        checkNotNull(name) { throw IllegalArgumentException("El nombre no puede ser nulo") }
        checkNotNull(userName) { throw IllegalArgumentException("El nombre de usuario no puede ser nulo") }
        checkNotNull(email) { throw IllegalArgumentException("El email no puede ser nulo") }
        checkNotNull(password) { throw IllegalArgumentException("La contraseña no puede ser nula") }

        return this;
    }
}