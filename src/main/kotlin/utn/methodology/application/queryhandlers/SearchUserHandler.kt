package utn.methodology.application.queryhandlers
import utn.methodology.application.queries.SearchUserQuery
import utn.methodology.domain.entities.contracts.UserRepository
import utn.methodology.domain.entities.entity.User

class SearchUserHandler(
    private val userRepository: UserRepository,
) {
    fun handle(query: SearchUserQuery): User {
        return userRepository.findByUsername(query.username)
            ?: throw NotFoundException("Usuario no encontrado con username: ${query.username}")
    }

}
class NotFoundException(message: String) : RuntimeException(message)