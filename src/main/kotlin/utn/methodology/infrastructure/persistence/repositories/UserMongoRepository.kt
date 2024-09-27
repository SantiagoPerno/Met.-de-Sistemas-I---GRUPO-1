package utn.methodology.infrastructure.persistence.repositories

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.UpdateOptions
import io.github.cdimascio.dotenv.dotenv
import utn.methodology.domain.entities.contracts.UserRepository
import utn.methodology.infrastructure.http.routes.healthRoutes
import utn.methodology.domain.entities.entity.User
import org.bson.Document
import utn.methodology.infrastructure.http.routes.userRoutes
import javax.print.attribute.standard.JobOriginatingUserName

val collectionName: String = dotenv()["USER_COLLECTION_NAME"] ?: "user"

class UserMongoRepository(private val database: MongoDatabase) : UserRepository {

    private var collection: MongoCollection<Document>

    init {
        collection = this.database.getCollection("users")
    }

    override fun save(user: User) {
        println("UserMongoRepository - Saving user: $user")
        val options = UpdateOptions().upsert(true)
        val filter = Document("_id", user.uuid) // Usa el campo uuid como filtro
        val update = Document("\$set", user.toPrimitives())

        collection.updateOne(filter, update, options)
    }

    override fun findOne(id: String): User? {
        val filter = Document("_id", id);
        val document = collection.find(filter).firstOrNull();

        return document?.let {
            User.fromPrimitives(mapFromDocument(it))
        }
    }

    override fun findByEmail(email: String): User? {
        val filter = Document("email", email);
        val document = collection.find(filter).firstOrNull();

        return document?.let {
            User.fromPrimitives(mapFromDocument(it))
        }
    }

    override fun findByUserName(userName: String): User? {
        val filter = Document("userName", userName)
        val document = collection.find(filter).firstOrNull()

        return document?.let {
            User.fromPrimitives(mapFromDocument(it))
        }
    }

    private fun mapFromDocument(document: Document): Map<String, String> {
        return document.entries.associate { it.key to it.value.toString() }
    }
}