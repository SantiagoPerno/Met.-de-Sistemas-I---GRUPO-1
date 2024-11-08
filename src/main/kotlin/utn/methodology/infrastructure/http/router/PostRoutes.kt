package utn.methodology.infrastructure.http.router

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import utn.methodology.application.commandhandlers.PostCommandHandler
import utn.methodology.application.commandhandlers.PostQueryHandler
import utn.methodology.application.models.FollowRequest
import utn.methodology.application.models.PostRequest

fun Route.postRoutes(
    postCommandHandler: PostCommandHandler,
    postQueryHandler: PostQueryHandler
) {
    route("/posts") {
        // Ruta para crear un Post (POST /posts)
        post {
            try {
                val postRequest = call.receive<PostRequest>()

                // Validaciones sencillas
                if (postRequest.userId.isEmpty() || postRequest.message.isEmpty()) {
                    call.respond(HttpStatusCode.BadRequest, "userId y mensaje son requeridos")
                    return@post
                }

                // Llamar al handler para crear el post
                val result = postCommandHandler.createPost(postRequest)
                call.respond(HttpStatusCode.Created, "Post generado")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, e.message ?: "Error interno al crear el post")
            }
        }

        // Ruta para buscar posts de un usuario (GET /posts)
        get {
            try {
                val userId = call.request.queryParameters["userId"]
                val order = call.request.queryParameters["order"] ?: "DESC"
                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10
                val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0

                if (userId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.BadRequest, "userId es requerido")
                    return@get
                }

                val posts = postQueryHandler.getUserPosts(userId, order, limit, offset)
                call.respond(HttpStatusCode.OK, posts)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, e.message ?: "Error en la búsqueda de posts")
            }
        }

        // Ruta para eliminar un post (DELETE /posts/{id})
        delete("{id}"){
            try {
                val postId = call.parameters["id"]

                if (postId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.BadRequest, "Post ID es requerido")
                    return@delete
                }

                val result = postCommandHandler.deletePost(postId)
                if (result) {
                    call.respond(HttpStatusCode.OK, "Post eliminado")
                } else {
                    call.respond(HttpStatusCode.NotFound, "Post no encontrado")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, e.message ?: "Error al eliminar el post")
            }
        }

        get("/followed/{userId}") {
            try {
                val userId = call.parameters["userId"]

                if (userId.isNullOrEmpty()) {
                    call.respond(HttpStatusCode.BadRequest, "userId es requerido")
                    return@get
                }

                val posts = postQueryHandler.getFollowedUserPosts(userId)
                call.respond(HttpStatusCode.OK, posts)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, e.message ?: "Error al obtener posts de usuarios seguidos")
            }
        }
    }

    // Ruta para seguir a un usuario (POST /follow)
    route("/follow") {
        post {
            try {
                val followRequest = call.receive<FollowRequest>()

                if (followRequest.userId.isEmpty() || followRequest.followedUserId.isEmpty()) {
                    call.respond(HttpStatusCode.BadRequest, "Ambos userId y followedUserId son requeridos")
                    return@post
                }

                val result = postCommandHandler.followUser(followRequest.userId, followRequest.followedUserId)
                call.respond(HttpStatusCode.OK, result)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, e.message ?: "Error al seguir al usuario")
            }
        }
    }
}
