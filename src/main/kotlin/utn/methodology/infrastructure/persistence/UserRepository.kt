package utn.methodology.infrastructure.persistence

import org.litote.kmongo.coroutine.CoroutineDatabase
import utn.methodology.application.models.User

class UserRepository(private val db: CoroutineDatabase) {

    private val users = db.getCollection<User>()

    suspend fun save(user: User): String {
        users.insertOne(user)
        return user.id
    }

    suspend fun findByUsername(username: String): User? {
        return users.findOne("{ username: '$username' }")
    }
}
