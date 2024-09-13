package utn.methodology.infrastructure.persistence

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.UpdateOptions
import org.bson.Document
import utn.methodology.domain.entities.entity.User
import utn.methodology.domain.entities.contracts.UserRepository



class MongoUserRepository(private val database: MongoDatabase) : UserRepository {
    private val collection: MongoCollection<Document>

    init {
        collection = database.getCollection("users")
    }

    override fun save(user: User) {
        val options = UpdateOptions().upsert(true)
        val filter = Document("_uuid", user.getId())
        val update = Document("\$set", user.toPrimitives())

        collection.updateOne(filter, update, options)
    }

    override fun findOne(id: String): User? {
        val filter = Document("_uuid", id)
        val document = collection.find(filter).firstOrNull() ?: return null

        return User.fromPrimitives(document.toMap() as Map<String, String>)
    }

    override fun existByEmail(email: String): Boolean {
        val filter = Document("email", email)
        return collection.find(filter).firstOrNull() != null
    }

    override fun findByUsername(username: String): User? {
        val filter = Document("username", username)
        val document = collection.find(filter).firstOrNull() ?: return null
        return User.fromPrimitives(document.toMap() as Map<String, String>)
    }
}

