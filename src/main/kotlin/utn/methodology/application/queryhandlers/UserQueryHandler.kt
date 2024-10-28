package utn.methodology.application.queryhandlers

import utn.methodology.application.models.SearchUserQuery
import utn.methodology.application.models.User
import utn.methodology.infrastructure.persistence.UserRepository

class UserQueryHandler(private val userRepository: UserRepository) {

    suspend fun handle(query: SearchUserQuery): User? {
        return userRepository.findByUsername(query.username)
    }
}
