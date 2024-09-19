package utn.methodology.infrastructure.http.router
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import utn.methodology.domain.entities.entity.User //Para relacionar con la clase User



data class CreateUserRequest(
    val name: String,
    val username: String,
    val email: String,
    val password: String
)


var users = mutableListOf<User>()
fun Route.userRoutes() {
    route("/users") {
        post("/create") {
            val userRequest = call.receive<CreateUserRequest>()
            // Validación básica
            if (userRequest.username.isBlank() || userRequest.email.isBlank() || userRequest.password.isBlank()) {
                call.respond(HttpStatusCode.BadRequest, "All fields are required")
                return@post
            }
            // Crear un nuevo usuario
            val newUser = User(
                name = userRequest.name,
                username = userRequest.username,
                email = userRequest.email,
                password = userRequest.password
            )
            // Agregar el usuario a la lista (esto es solo un ejemplo, deberías persistir en una base de datos)
            users.add(newUser)
            call.respond(HttpStatusCode.Created, newUser)
        }
    }
}