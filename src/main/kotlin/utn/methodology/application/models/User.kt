package utn.methodology.application.models

import org.bson.codecs.pojo.annotations.BsonId

data class User(
    @BsonId val id: String,
    val username: String,
    val email: String,
    val password: String
)
