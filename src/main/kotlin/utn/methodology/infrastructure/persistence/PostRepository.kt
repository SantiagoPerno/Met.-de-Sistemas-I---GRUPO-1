package utn.methodology.infrastructure.persistence

import com.mongodb.client.model.Indexes.ascending
import com.mongodb.client.model.Indexes.descending
import org.litote.kmongo.ascending
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.descending
import org.litote.kmongo.eq
import org.litote.kmongo.`in`
import utn.methodology.application.models.Follow
import utn.methodology.application.models.Post

class PostRepository(private val db: CoroutineDatabase) {

    private val posts = db.getCollection<Post>()
    private val follows = db.getCollection<Follow>()

    suspend fun save(post: Post): String {
        posts.insertOne(post)
        return post.id
    }

    suspend fun delete(postId: String): Boolean {
        val result = posts.deleteOneById(postId)
        return result.deletedCount > 0
    }

    suspend fun findPostsByUser(userId: String, order: String, limit: Int, offset: Int): List<Post> {
        val sortOrder = if (order == "ASC") ascending(Post::createdAt) else descending(Post::createdAt)
        return posts.find(Post::userId eq userId)
            .sort(sortOrder)
            .limit(limit)
            .skip(offset)
            .toList()
    }

    suspend fun findPostsByFollowedUsers(userId: String): List<Post> {
        val followedUsers = follows.find(Follow::userId eq userId).toList()
        val followedIds = followedUsers.map { it.followedUserId }

        return posts.find(Post::userId `in` followedIds)
            .sort(descending(Post::createdAt))
            .toList()
    }

    suspend fun addFollower(userId: String, followedUserId: String): Boolean {
        follows.insertOne(Follow(userId = userId, followedUserId = followedUserId))
        return true
    }
}
