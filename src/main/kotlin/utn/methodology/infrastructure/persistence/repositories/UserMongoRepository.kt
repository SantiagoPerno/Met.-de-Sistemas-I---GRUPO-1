package utn.methodology.infrastructure.persistence.repositories

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.UpdateOptions
import io.github.cdimascio.dotenv.dotenv
import utn.methodology.domain.entities.contracts.UserRepository
import utn.methodology.domain.entities.entity.User
import org.bson.Document
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
        val primitives = collection.find(filter).firstOrNull();

        if (primitives == null) {
            return null;
        }

        return User.fromPrimitives(primitives as Map<String, String>)
    }

    override fun findByEmal(email: String): User? {
        val filter = Document("email", email);
        val primitives = collection.find(filter).firstOrNull();

        if(primitives == null){
            return null;
        }

        return User.fromPrimitives(primitives as Map<String, String>)
    }

    override fun findByUserName(userName: String): User? {
        val filter = Document("userName", userName);
        val primitives = collection.find(filter).firstOrNull();

        if(primitives == null){
            return null;
        }

        return User.fromPrimitives(primitives as Map<String, String>)
    }
}