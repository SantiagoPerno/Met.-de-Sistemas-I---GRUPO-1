package utn.methodology.application.commandhandlers

import utn.methodology.domain.entities.commands.createUserCommand
import utn.methodology.domain.entities.entity.User
import utn.methodology.domain.entities.contracts.UserRepository

class CreateUserHandler(
    private val userRepository: UserRepository,
) {
    fun handle(command: createUserCommand) {
        if (userRepository.existByEmail(command.email)) {
            throw ConflictException("El email ya existe")
        }

        val user = User(
            name = command.name,
            username = command.username,
            email = command.email,
            password = command.password
        )

        userRepository.save(user)
    }
}

class ConflictException(message: String) : RuntimeException(message)
