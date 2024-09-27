package utn.methodology.infrastructure.http.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import utn.methodology.eventBus
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.request.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*


import utn.methodology.application.commandhandlers.CreateUserHandler
import utn.methodology.application.commands.CreateUserCommand
import utn.methodology.infrastructure.http.actions.ConfirmUserAction
import utn.methodology.application.commandhandlers.SearchUserHandler
import utn.methodology.application.queries.SearchUserQuery
import utn.methodology.application.actions.SearchUserAction
import utn.methodology.infrastructure.persistence.config.connectToMongoDB
import utn.methodology.infrastructure.persistence.repositories.UserMongoRepository


fun Application.userRoutes() {
    val mongoDatabase = connectToMongoDB()

    val userMongoRepository = UserMongoRepository(mongoDatabase)

    val confirmUserAction = ConfirmUserAction(CreateUserHandler(userMongoRepository))
    val searchUserAction = SearchUserAction(SearchUserHandler(userMongoRepository))

    routing {
        post("/users") {
            println("Received POST request to /users")

            val body = call.receive<CreateUserCommand>()
            confirmUserAction.execute(body)

            call.respond(HttpStatusCode.Created, mapOf("message" to "ok"))
        }

        get("/users") {
            println("Received GET request to /users")
            val userName = call.request.queryParameters["userName"]

            if (userName.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, "Username is required")
                return@get
            }

            val query = SearchUserQuery(userName)
            val result = searchUserAction.execute(query) // Aquí es donde usas searchUserAction

            // Responde según el resultado de la búsqueda
            if (result != null) {
                call.respond(HttpStatusCode.OK, result)
            } else {
                call.respond(HttpStatusCode.NotFound, "User not found")
            }
        }
    }
}