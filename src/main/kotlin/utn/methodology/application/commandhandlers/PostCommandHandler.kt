package utn.methodology.application.commandhandlers

import utn.methodology.application.models.Post
import utn.methodology.application.models.PostRequest
import utn.methodology.infrastructure.persistence.PostRepository

class PostCommandHandler(private val postRepository: PostRepository) {

        suspend fun createPost(request: PostRequest): String {
            val newPost = Post(
                userId = request.userId,
                message = request.message,
                createdAt = System.currentTimeMillis()
            )
            return postRepository.save(newPost)
        }

        suspend fun deletePost(postId: String): Boolean {
            return postRepository.delete(postId)
        }

        suspend fun followUser(userId: String, followedUserId: String): Boolean {
            return postRepository.addFollower(userId, followedUserId)
        }
    }

