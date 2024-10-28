package utn.methodology.application.queryhandlers

import com.example.models.SearchUserQuery
import com.example.repositories.UserRepository

class UserQueryHandler(private val userRepository: UserRepository) {

    suspend fun handle(query: SearchUserQuery): User? {
        return userRepository.findByUsername(query.username)
    }
}
