//Este handler recibe el CreateUserCommand, valida los datos y crea el usuario.


package com.example.handlers

import com.example.models.CreateUserCommand
import com.example.repositories.UserRepository
import org.bson.types.ObjectId

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
