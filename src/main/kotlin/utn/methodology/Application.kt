package utn.methodology


import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.serialization.gson.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import utn.methodology.application.commandhandlers.UserCommandHandler
import utn.methodology.application.queryhandlers.UserQueryHandler
import utn.methodology.infrastructure.http.router.userRoutes
import utn.methodology.infrastructure.persistence.UserRepository



fun main() {
    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
            }
        }
        install(StatusPages)

        val client = KMongo.createClient().coroutine
        val database = client.getDatabase("my_database")

        val userRepository = UserRepository(database)
        val userCommandHandler = UserCommandHandler(userRepository)
        val userQueryHandler = UserQueryHandler(userRepository)

        routing {
            userRoutes(userCommandHandler, userQueryHandler)
        }
    }.start(wait = true)
}

/*
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
        json()
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    configureDatabases()
    //userRouter()
    errorHandler()
}



fun logError(call: ApplicationCall, cause: Throwable) {
    val log = LoggerFactory.getLogger("ErrorLogger")
    val requestUri = call.request.uri
    log.error("Error at $requestUri: ${cause.localizedMessage}", cause)
}
 */