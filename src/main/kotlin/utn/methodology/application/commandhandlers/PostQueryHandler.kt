package utn.methodology.application.commandhandlers

import utn.methodology.application.models.Post
import utn.methodology.infrastructure.persistence.PostRepository

class PostQueryHandler(private val postRepository: PostRepository) {

    suspend fun getUserPosts(userId: String, order: String, limit: Int, offset: Int): List<Post> {
        return postRepository.findPostsByUser(userId, order, limit, offset)
    }

    suspend fun getFollowedUserPosts(userId: String): List<Post> {
        return postRepository.findPostsByFollowedUsers(userId)
    }
}
