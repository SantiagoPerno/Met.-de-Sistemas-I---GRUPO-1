package utn.methodology.infrastructure.http.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import utn.methodology.eventBus
import utn.methodology.application.commandhandlers.CreateUserHandler
import utn.methodology.application.commands.CreateUserCommand
import utn.methodology.infrastructure.http.actions.ConfirmUserAction
import utn.methodology.infrastructure.persistence.config.connectToMongoDB
import utn.methodology.infrastructure.persistence.repositories.UserMongoRepository


fun Application.userRoutes() {
    val mongoDatabase = connectToMongoDB()

    val userMongoRepository = UserMongoRepository(mongoDatabase)

    val confirmUserAction =
        ConfirmUserAction(CreateUserHandler(userMongoRepository))

    routing {
        // Ruta (route) ----> ejecuta una accion en base a un path al que se requiere
        //      Acción (action) ----> valida el body recibido y se lo pasa al handler
        //                  Manejador/Orquestador (Handler) ----> ejecuta la lógica de negocio
        //                                  Repositorio (Repository) ----> se comunica con la base de datos
        //                                                  Entidad (Entity) ----> mapea los datos previos a la consulta a la base de datos

        // POST /shippings crea un nuevo envío
        post("/users") {
            println("Received POST request to /user")

            val body = call.receive<CreateUserCommand>()

            confirmUserAction.execute(body)

            call.respond(HttpStatusCode.Created, mapOf("message" to "ok"))
        }
    }
}