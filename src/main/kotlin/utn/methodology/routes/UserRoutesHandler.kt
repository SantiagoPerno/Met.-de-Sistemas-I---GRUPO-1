package utn.methodology.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import utn.methodology.application.commands.CreateUserCommand
import utn.methodology.application.queries.SearchUserQuery
import utn.methodology.application.queryhandlers.SearchUserHandler
import utn.methodology.domain.entities.commands.createUserCommand
import utn.methodology.domain.entities.contracts.UserRepository
import utn.methodology.domain.entities.handlers.CreateUserHandler
import utn.methodology.infrastructure.persistence.connectToMongoDB
import utn.methodology.infrastructure.persistence.MongoUserRepository



fun Route.userRoutes(application: Application) {
    route("/users") {
        post {
            try {
                val userRequest = call.receive<CreateUserCommand>()
                println("Received user request: $userRequest")

                val handler = CreateUserHandler()
                val newUser = handler.handle(userRequest)
                call.respond(HttpStatusCode.Created, newUser)
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "Invalid request")
            } catch (e: Exception) {
                println("Error: ${e.message}")
                call.respond(HttpStatusCode.InternalServerError, "An unexpected error occurred")
            }
        }

        get {
            try {
                val username = call.parameters["username"]
                if (username.isNullOrBlank()) {
                    call.respond(HttpStatusCode.BadRequest, "Username es requerido")
                    return@get
                }

                val database = application.connectToMongoDB()
                val userRepository = MongoUserRepository(database)
                val searchUserHandler = SearchUserHandler(userRepository)

                val user = searchUserHandler.handle(SearchUserQuery(username))
                if (user == null) {
                    call.respond(HttpStatusCode.NotFound, "Usuario no encontrado")
                } else {
                    call.respond(HttpStatusCode.OK, user.toPrimitives())
                }

            } catch (e: Exception) {
                println("Error: ${e.message}")
                call.respond(HttpStatusCode.InternalServerError, "An unexpected error occurred")
            }
        }

    }
}
