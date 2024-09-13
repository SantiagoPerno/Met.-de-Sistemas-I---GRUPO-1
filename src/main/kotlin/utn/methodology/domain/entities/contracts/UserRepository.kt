package utn.methodology.domain.entities.contracts

import utn.methodology.domain.entities.entity.User

interface UserRepository {
    fun save(user: User)
    fun findOne(id: String): User?
    fun existByEmail(email: String): Boolean
    fun findByUsername(username: String): User?
}