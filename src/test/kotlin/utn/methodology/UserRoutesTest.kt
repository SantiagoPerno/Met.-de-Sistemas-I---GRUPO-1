package utn.methodology

import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import utn.methodology.application.commandhandlers.UserCommandHandler
import utn.methodology.infrastructure.persistence.UserRepository
import utn.methodology.infrastructure.http.router.userRoutes
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.gson.*
import io.ktor.server.routing.*
import io.mockk.mockk
import utn.methodology.application.queryhandlers.UserQueryHandler




class UserRoutesTest {


    @Test
    fun createUser_shouldReturn201() {
        withTestApplication({
            // Simular la configuración de la aplicación
            install(ContentNegotiation) {
                gson { setPrettyPrinting() }
            }
            //mockk or provide a test database connection
            val userRepository = mockk<UserRepository>(relaxed = true)  /* DB simulada o en memoria */
            val userCommandHandler = UserCommandHandler(userRepository)
            val userQueryHandler = mockk<UserQueryHandler>(relaxed = true)
            routing {
                userRoutes(userCommandHandler, userQueryHandler)
            }
        }) {
            // Simular el request con datos válidos
            handleRequest(HttpMethod.Post, "/users") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(
                    """
                   {
                       "username": "testuser",
                       "email": "testuser@example.com",
                       "password": "password123"
                   }
               """.trimIndent()
                )
            }.apply {
                assertEquals(HttpStatusCode.Created, response.status())
                // Aquí puedes verificar el contenido del body si es necesario
            }
        }
    }


    @Test
    fun createUser_shouldReturn400() {
        withTestApplication({
            // Simular la configuración de la aplicación
            install(ContentNegotiation) {
                gson { setPrettyPrinting() }
            }


            val userRepository = mockk<UserRepository>(relaxed = true) /* Inyectar DB simulada o en memoria */
            val userCommandHandler = UserCommandHandler(userRepository)
            val userQueryHandler = mockk<UserQueryHandler>(relaxed = true)
            routing {
                userRoutes(userCommandHandler, userQueryHandler)
            }
        }) {
            // Simular el request con datos faltantes o incorrectos
            handleRequest(HttpMethod.Post, "/users") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(
                    """
                   {
                       "username": "",
                       "email": "invalid-email",
                       "password": ""
                   }
               """.trimIndent()
                )
            }.apply {
                assertEquals(HttpStatusCode.BadRequest, response.status())
            }
        }
    }
}
