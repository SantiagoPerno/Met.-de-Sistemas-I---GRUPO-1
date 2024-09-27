package utn.methodology.application.commandhandlers

import utn.methodology.domain.entities.contracts.UserRepository
import utn.methodology.application.queries.SearchUserQuery
import utn.methodology.application.dtos.UserDTO
import javax.management.Query

class SearchUserHandler(private val userRepository: UserRepository) {
    fun handle(query: SearchUserQuery): UserDTO? {
        val user = userRepository.findByUserName(query.username)
        return user?.let {
            UserDTO(it.uuid, it.name, it.email)
        }
    }
}