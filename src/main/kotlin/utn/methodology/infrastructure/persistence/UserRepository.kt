package com.example.repositories

import com.example.models.User
import org.litote.kmongo.coroutine.CoroutineDatabase

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
