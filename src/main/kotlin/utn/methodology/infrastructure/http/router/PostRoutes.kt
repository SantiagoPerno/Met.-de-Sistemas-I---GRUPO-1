package utn.methodology.infrastructure.http.router


import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import utn.methodology.application.commandhandlers.PostCommandHandler
import utn.methodology.application.commandhandlers.PostQueryHandler
import utn.methodology.application.models.FollowRequest
import utn.methodology.application.models.PostRequest

fun Route.postRoutes(postCommandHandler: PostCommandHandler, postQueryHandler: PostQueryHandler) {

    // Ruta para crear un Post (POST /posts)
    post("/posts") {
        val postRequest = call.receive<PostRequest>()

        // Validaciones sencillas
        if (postRequest.userId.isNullOrEmpty() || postRequest.message.isNullOrEmpty()) {
            call.respond(HttpStatusCode.BadRequest, "userId y mensaje son requeridos")
            return@post
        }

        // Llamar al handler para crear el post
        val result = postCommandHandler.createPost(postRequest)
        call.respond(HttpStatusCode.Created, result)
    }

    // Ruta para buscar posts de un usuario (GET /posts)
    get("/posts") {
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
    }

    // Ruta para eliminar un post (DELETE /posts/{id})
    delete("/posts/{id}") {
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
    }

    // Ruta para ver los posts de los usuarios seguidos (GET /posts/user/{userId})
    get("/posts/user/{userId}") {
        val userId = call.parameters["userId"]

        if (userId.isNullOrEmpty()) {
            call.respond(HttpStatusCode.BadRequest, "userId es requerido")
            return@get
        }

        val posts = postQueryHandler.getFollowedUserPosts(userId)
        call.respond(HttpStatusCode.OK, posts)
    }

    // Ruta para seguir a un usuario (POST /follow)
    post("/follow") {
        val followRequest = call.receive<FollowRequest>()

        if (followRequest.userId.isNullOrEmpty() || followRequest.followedUserId.isNullOrEmpty()) {
            call.respond(HttpStatusCode.BadRequest, "Ambos userId y followedUserId son requeridos")
            return@post
        }

        val result = postCommandHandler.followUser(followRequest.userId, followRequest.followedUserId)
        call.respond(HttpStatusCode.OK, result)
    }
}
