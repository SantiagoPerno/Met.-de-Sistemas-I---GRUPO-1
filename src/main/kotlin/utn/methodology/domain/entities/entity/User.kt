package utn.methodology.domain.entities.entity
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val uuid: Int = generateUuid(),
    val name: String,
    val username: String,
    var email: String,
    var password: String
) {
    companion object {
        private var lastUuid: Int = 0

        private fun generateUuid(): Int {
            lastUuid += 1
            return lastUuid
        }

        fun fromPrimitives(primitives: Map<String, String>): User {
            return User(
                primitives["uuid"]?.toInt() ?: 0,
                primitives["name"] ?: "",
                primitives["username"] ?: "",
                primitives["email"] ?: "",
                primitives["password"] ?: ""
            )
        }
    }


    fun getId(): String {
        return this.uuid.toString()
    }

    fun toPrimitives(): Map<String, String> {
        return mapOf(
            "uuid" to this.uuid.toString(),
            "name" to this.name,
            "username" to this.username,
            "email" to this.email,
            "password" to this.password
        )
    }
}






