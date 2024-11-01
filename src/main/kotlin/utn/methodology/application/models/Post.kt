package utn.methodology.application.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Post(
    @BsonId val id: String = ObjectId().toString(),
    val userId: String,
    val message: String,
    val createdAt: Long
)
