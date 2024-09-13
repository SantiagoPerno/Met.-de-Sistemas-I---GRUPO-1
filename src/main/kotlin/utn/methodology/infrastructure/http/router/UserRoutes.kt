package utn.methodology.infrastructure.http.router


import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import utn.methodology.application.commandhandlers.ConflictException
import utn.methodology.domain.entities.commands.createUserCommand
import utn.methodology.application.commandhandlers.CreateUserHandler
import utn.methodology.application.queries.SearchUserQuery
import utn.methodology.application.queryhandlers.NotFoundException
import utn.methodology.application.queryhandlers.SearchUserHandler
import utn.methodology.domain.entities.contracts.UserRepository

fun Route.userRoutes(userRepository: UserRepository) {
    route("/users") {
        post("/create") {
            val command = call.receive<createUserCommand>()
            try {
                val handler = CreateUserHandler(userRepository)
                handler.handle(command)
                call.respond(HttpStatusCode.Created, "User created successfully")
            } catch (e: ConflictException) {
                call.respond(HttpStatusCode.Conflict, e.message ?: "Email already exists")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "An unexpected error occurred")
            }
        }
    }
}

fun Route.searchUserRoute(searchUserHandler: SearchUserHandler) {
    get("/users") {
        val username = call.request.queryParameters["username"]
            ?: throw BadRequestException("Username no proporcionado")

        try {
            val user = searchUserHandler.handle(SearchUserQuery(username))
            call.respond(user.toPrimitives())  // Devuelve la respuesta en formato JSON
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Parámetro no válido")
        } catch (e: NotFoundException) {
            call.respond(HttpStatusCode.NotFound, e.message ?: "Usuario no encontrado")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Error inesperado")
        }
    }
}

class BadRequestException(message: String) : RuntimeException(message)


