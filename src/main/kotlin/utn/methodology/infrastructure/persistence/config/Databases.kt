package utn.methodology.infrastructure.persistence.config

import com.mongodb.client.*
import io.github.cdimascio.dotenv.dotenv
import io.ktor.server.application.*
import io.ktor.server.config.*

fun Application.connectToMongoDB(): MongoDatabase {
    val user = System.getenv("MONGO_INITDB_ROOT_USERNAME") ?: "root"
    val password = System.getenv("MONGO_INITDB_ROOT_PASSWORD") ?: "example"
    val host = System.getenv("MONGO_DB_HOST") ?: "localhost"
    val port = System.getenv("MONGO_DB_PORT")?.toInt() ?: 27017
    val maxPoolSize = System.getenv("MONGO_DB_MAX_POOL_SIZE")?.toInt() ?: 50
    val databaseName = System.getenv("MONGO_DATABASE_NAME") ?: "users"

    val credentials = "$user:$password@"
    val uri = "mongodb://$user:$password@$host:$port/?maxPoolSize=$maxPoolSize&w=majority&authSource=admin"

    println(uri)

    val mongoClient = MongoClients.create(uri)
    val database = mongoClient.getDatabase(databaseName)

    environment.monitor.subscribe(ApplicationStopped) {
        mongoClient.close()
    }

    return database
}