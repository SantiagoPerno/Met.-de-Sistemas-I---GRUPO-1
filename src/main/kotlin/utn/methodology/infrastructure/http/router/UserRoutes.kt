package utn.methodology.infrastructure.http.router

import utn.methodology.application.commandhandlers.UserCommandHandler
import utn.methodology.application.models.CreateUserCommand
import utn.methodology.application.models.SearchUserQuery
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import utn.methodology.application.queryhandlers.UserQueryHandler
import kotlin.text.get

fun Route.userRoutes(userCommandHandler: UserCommandHandler, userQueryHandler: UserQueryHandler) {
    // Ruta para la creación de usuarios (POST /users)
    route("/users") {
        post {
            try {
                val createUserRequest = call.receive<CreateUserCommand>()
                // Validaciones básicas
                if (createUserRequest.username.isNullOrEmpty() ||
                    createUserRequest.email.isNullOrEmpty() ||
                    createUserRequest.password.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.BadRequest, "Datos inválidos")
                    return@post
                }
                // Enviar el command al handler
                val result = userCommandHandler.handle(createUserRequest)
                call.respond(HttpStatusCode.Created, result)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, e.message ?: "Error interno")
            }
        }
        // Ruta para la búsqueda de usuario por username (GET /users?username=)
        get {
            val username = call.request.queryParameters["username"]
            if (username.isNullOrEmpty()) {
                call.respond(HttpStatusCode.BadRequest, "Username es requerido")
                return@get
            }
            try {
                // Crear query y enviarla al handler
                val searchQuery = SearchUserQuery(username)
                val user = userQueryHandler.handle(searchQuery)
                if (user != null) {
                    call.respond(HttpStatusCode.OK, user)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Usuario no encontrado")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, e.message ?: "Error en la búsqueda")
            }
        }
    }
}
