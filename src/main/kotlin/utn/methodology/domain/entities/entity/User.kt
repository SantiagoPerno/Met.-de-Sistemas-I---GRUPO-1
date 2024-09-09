package utn.methodology.domain.entities.entity

class User (
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

            val user = User(
                primitives["uuid"] as Int,
                primitives["name"] as String,
                primitives["username"] as String,
                primitives["email"] as String,
                primitives["password"] as String
            );

            return user;
        }
    }

    fun getId(): String {
        return this.uuid.toString();
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