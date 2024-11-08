package utn.methodology

import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.gson.*
import io.ktor.server.routing.*
import io.mockk.mockk
import org.litote.kmongo.json
import utn.methodology.application.commandhandlers.PostCommandHandler
import utn.methodology.application.commandhandlers.PostQueryHandler
import utn.methodology.infrastructure.http.router.postRoutes
import utn.methodology.infrastructure.persistence.PostRepository


class PostRoutesTest {

    @Test
    fun create_post_should_return_201() {
        withTestApplication({
            install(ContentNegotiation) {
                gson { setPrettyPrinting() }
            }
            val postRepository = mockk<PostRepository>(relaxed = true)
            val postQueryHandler = mockk<PostQueryHandler>(relaxed = true)
            val postCommandHandler = PostCommandHandler(postRepository)
            routing {
                postRoutes(postCommandHandler, postQueryHandler)
            }
        }) {
            handleRequest(HttpMethod.Post, "/posts") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(
                    """
                        {
                            "userId": "Usuario1",
                            "message": "Hola mundo"
                        }""".trimIndent()
                )
            }.apply {
                assertEquals(HttpStatusCode.Created, response.status())
                assertEquals("Post generado", response.content)
            }
        }
    }

    @Test
    fun create_post_should_return_400() {
        withTestApplication({
            install(ContentNegotiation) {
                gson { setPrettyPrinting() }
            }

            val postRepository = mockk<PostRepository>(relaxed = true)
            val postQueryHandler = mockk<PostQueryHandler>(relaxed = true)
            val postCommandHandler = PostCommandHandler(postRepository)
            routing {
                postRoutes(postCommandHandler, postQueryHandler)
            }
        }) {
            handleRequest(HttpMethod.Post, "/posts") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(
                    """
                        {
                            "userId": "",
                            "message": ""
                            }
                    """.trimIndent()
                )
            }.apply {
                assertEquals(HttpStatusCode.BadRequest, response.status())
            }
        }
    }  
}
