package utn.methodology.application.commandhandlers

import utn.methodology.application.commands.CreateUserCommand
import utn.methodology.domain.entities.entity.User
import utn.methodology.domain.entities.contracts.UserRepository
import utn.methodology.eventBus
import utn.methodology.infrastructure.persistence.repositories.UserMongoRepository
import java.util.*

class CreateUserHandler(
    private val userRepository: UserRepository,
){
    fun handle(command: CreateUserCommand) {

        if(userRepository.existByEmail(command.email)) {
            throw ConflictException("El email ya existe")
        }

        val user = User.create(
            command.uuid,
            command.name,
            command.userName,
            command.email,
            command.password
        )
        userRepository.save(user)
        eventBus.publish(user.pullDomainEvents())
    }
}

class ConflictException(message: String) : RuntimeException(message)