package utn.methodology.infrastructure.persistence.config

import com.mongodb.client.*
import io.github.cdimascio.dotenv.dotenv
import io.ktor.server.application.*
import io.ktor.server.config.*

fun Application.connectToMongoDB(): MongoDatabase {
    val user = dotenv()["MONGO_INITDB_ROOT_USERNAME"] ?: "root"
    val password = dotenv()["MONGO_INITDB_ROOT_PASSWORD"] ?: "example"
    val host = dotenv()["MONGO_DB_HOST"] ?: "localhost"
    val port = dotenv()["MONGO_DB_PORT"]?.toInt() ?: 27017
    val maxPoolSize = dotenv()["MONGO_DB_MAX_POOL_SIZE"]?.toInt() ?: 20
    val databaseName = dotenv()["MONGO_DATABASE_NAME"] ?: "users"

    val credentials = user.let { userVal -> password.let { passwordVal -> "$userVal:$passwordVal@" } }.orEmpty()

    println("credentials: $credentials")

    val uri = "mongodb://$credentials$host:$port/?maxPoolSize=$maxPoolSize&w=majority"

    val mongoClient = MongoClients.create(uri)
    val database = mongoClient.getDatabase(databaseName)

    environment.monitor.subscribe(ApplicationStopped) {
        mongoClient.close()
    }

    return database
}