package utn.methodology.domain.entities.contracts

import utn.methodology.domain.entities.entity.User

interface UserRepository {
    fun save(user: User)
    fun findOne(id:String): User?
    fun findByEmail(email:String): User?
    fun findByUserName(userName:String): User?
}