package utn.methodology

import com.fasterxml.jackson.databind.SerializationFeature
import utn.methodology.infrastructure.persistence.config.connectToMongoDB
import utn.methodology.infrastructure.http.routes.healthRoutes
import utn.methodology.infrastructure.eventBus.InMemoryEventBus
import utn.methodology.infrastructure.http.routes.userRoutes
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.slf4j.LoggerFactory


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

val eventBus = InMemoryEventBus()

fun Application.module() {
    install(ContentNegotiation) {
        json() // o utiliza el que prefieras
    }

    connectToMongoDB()
    healthRoutes()
    errorHandler()
    userRoutes()
}



fun logError(call: ApplicationCall, cause: Throwable) {
    val log = LoggerFactory.getLogger("ErrorLogger")
    val requestUri = call.request.uri
    log.error("Error at $requestUri: ${cause.localizedMessage}", cause)
}