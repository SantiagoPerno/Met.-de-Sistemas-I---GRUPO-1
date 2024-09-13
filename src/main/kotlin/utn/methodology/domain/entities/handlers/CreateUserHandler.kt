package utn.methodology.domain.entities.handlers

import utn.methodology.application.commands.CreateUserCommand
import utn.methodology.domain.entities.entity.User

class CreateUserHandler {

    fun handle(command: CreateUserCommand): User {
        if (command.username.isBlank() || command.email.isBlank() || command.password.isBlank()) {
            throw IllegalArgumentException("All fields are required")
        }

        return User(
            name = command.name,
            username = command.username,
            email = command.email,
            password = command.password
        )
    }
}
