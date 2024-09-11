package utn.methodology.infrastructure.http.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import utn.methodology.application.commandhandlers.CreateUserHandler
import utn.methodology.infrastructure.http.actions.ConfirmUserAction
import utn.methodology.infrastructure.persistence.repositories.UserMongoRepository
import utn.methodology.infrastructure.persistence.config.connectToMongoDB

fun Application.healthRoutes() {
    val mongoDatabase = connectToMongoDB() // Conexión a la base de datos

    val userMongoRepository = UserMongoRepository(mongoDatabase) // Inyección del repositorio

    val confirmUserAction =
        ConfirmUserAction(CreateUserHandler(userMongoRepository)) // Inyección del manejador de la acción

//    val findUserByIdAction = FindUserByIdAction(FindUserByIdHandler(userMongoUserRepository))

    routing {
        // Ruta (route) ----> ejecuta una accion en base a un path al que se requiere
        //      Acción (action) ----> valida el body recibido y se lo pasa al handler
        //                  Manejador/Orquestador (Handler) ----> ejecuta la lógica de negocio
        //                                  Repositorio (Repository) ----> se comunica con la base de datos
        //                                                  Entidad (Entity) ----> mapea los datos previos a la consulta a la base de datos

        // POST /shippings crea un nuevo envío
        get("/health") {
            call.respond(HttpStatusCode.OK,  mapOf("message" to "ok"))
        }
    }
}
