package utn.methodology

import com.mongodb.client.ListDatabasesIterable
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory
import utn.methodology.infrastructure.persistence.configureDatabases
import utn.methodology.routes.userRoutes
import kotlinx.serialization.json.Json
import utn.methodology.application.commandhandlers.CreateUserHandler
import utn.methodology.application.queryhandlers.SearchUserHandler
import utn.methodology.infrastructure.http.router.searchUserRoute
import utn.methodology.infrastructure.http.router.userRoutes
import utn.methodology.infrastructure.persistence.MongoUserRepository
import utn.methodology.infrastructure.persistence.connectToMongoDB

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.errorHandler() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            utn.methodology.logError(call, cause)

            if (cause is NotFoundException) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to cause.message))
            } else {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Internal server error"))
            }

        }
    }
}

fun Application.module() {
    install(ContentNegotiation) {
        json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            }
        )
    }

    configureDatabases()
    val userRepository = MongoUserRepository(connectToMongoDB())
    val searchUserHandler = SearchUserHandler(userRepository)

    routing {
        get("/") {

        }
        userRoutes(userRepository)
        searchUserRoute(searchUserHandler)
    }
    errorHandler()
}

fun logError(call: ApplicationCall, cause: Throwable) {
    val log = LoggerFactory.getLogger("ErrorLogger")
    val requestUri = call.request.uri
    log.error("Error at $requestUri: ${cause.localizedMessage}", cause)
}

