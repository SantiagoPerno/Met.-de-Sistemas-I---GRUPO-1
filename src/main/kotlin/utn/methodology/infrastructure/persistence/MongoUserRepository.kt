package utn.methodology.infrastructure.persistence

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.UpdateOptions
import io.ktor.server.engine.*
import utn.methodology.domain.entities.entity.User
import  org.bson.Document


class MongoUserRepository(private val database: MongoDatabase) {
    private val collection: MongoCollection<Any>;

    init{
        collection = database.getCollection("users") as MongoCollection<Any>;
    }

    fun save(user: User){
        val options = UpdateOptions().upsert((true));

        val filter = Document ("_uuid", user.getId())
        val update = Document("\$set", user.toPrimitives())

        collection.updateOne(filter, update, options)
    }

    override fun findOne(uuid: String): User?{
        val filter = Document("_uuid", uuid);

        val primitives = collection.find(filter).firstOrNull();

        if(primitives == null){
            return null;
        }

        return User.fromPrimitives(primitives as Map<String, String>)
    }

    fun findAll(): List<User>{
        val primitives = collection.find().map {it as Document}.toList();

        return primitives.map{
            User.fromPrimitives(it.toMap() as Map<String, String>)
        };
    }

    fun delete(user: User) {
        val filter = Document("_uuid", user.getId());

        collection.deleteOne(filter)
    }

    fun findByName(name: String): User?{
        val filter = Document("name", name);

        val primitives = collection.find(filter).firstOrNull();

        if(primitives == null){
            return null;
        }

        return User.fromPrimitives(primitives as Map<String, String>)
    }

    fun findByEmail(email: String): User?{
        val filter = Document("email", email);

        val primitives = collection.find(filter).firstOrNull();

        if(primitives == null){
            return null;
        }

        return User.fromPrimitives(primitives as Map<String, String>)
    }
}