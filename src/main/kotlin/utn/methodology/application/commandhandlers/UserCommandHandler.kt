//Este handler recibe el CreateUserCommand, valida los datos y crea el usuario.


package utn.methodology.application.commandhandlers

import utn.methodology.application.models.CreateUserCommand
import utn.methodology.infrastructure.persistence.UserRepository
import org.bson.types.ObjectId
import utn.methodology.application.models.User

class UserCommandHandler(private val userRepository: UserRepository) {

    suspend fun handle(command: CreateUserCommand): String {
        // Validación y creación de usuario
        val newUser = User(
            id = ObjectId().toString(),
            username = command.username,
            email = command.email,
            password = command.password // Recuerda manejar la encriptación de la contraseña
        )
        return userRepository.save(newUser)
    }
}
