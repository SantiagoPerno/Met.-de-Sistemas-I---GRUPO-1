package utn.methodology.domain.entities.entity

import utn.methodology.domain.entities.events.DomainEvent
import utn.methodology.domain.entities.events.UserCreated

data class User(
    val uuid: Int = generateUuid(),
    val name: String,
    val userName: String,
    var email: String,
    var password: String
) {
    private var events: List<DomainEvent> = mutableListOf()
    private var status: Status? = null

    init {
        validate()
    }

    companion object {
        private var lastUuid: Int = 0

        private fun generateUuid(): Int {
            lastUuid += 1
            return lastUuid
        }

        fun fromPrimitives(primitives: Map<String, Any>): User {
            val user = User(
                primitives["uuid"] as Int,
                primitives["name"] as String,
                primitives["userName"] as String, // Corregido a "username"
                primitives["email"] as String,
                primitives["password"] as String
            )
            user.validate()
            return user
        }

        fun create(
            uuid: Int,
            name: String,
            userName: String,
            email: String,
            password: String
        ): User {
            val user = User(
                uuid,
                name,
                userName,
                email,
                password
            )

            user.recordEvent(
                UserCreated(
                    user.uuid,
                    name,
                    userName,
                    email,
                    password
                )
            )

            user.status = Status("created", "pending")

            if (name.isNotBlank() || userName.isNotBlank() || email.isNotBlank() || password.isNotBlank()) {
                user.status = Status("created", "still_creating")
            }

            return user
        }
    }

    private fun validate() {
        require(name.isNotBlank()) { "El nombre no puede estar vacío" }
        require(userName.isNotBlank()) { "El nombre de usuario no puede estar vacío" }
        require(email.isNotBlank() && email.contains("@")) { "Email inválido" }
        require(password.isNotBlank() && password.length >= 8) { "La contraseña debe tener 8 o más caracteres" }
    }

    private fun recordEvent(event: DomainEvent) {
        this.events = this.events + event
    }

    fun toPrimitives(): Map<String, Any?> {
        return mapOf(
            "uuid" to this.uuid,
            "name" to this.name,
            "username" to this.userName,
            "email" to this.email,
            "password" to this.password
        )
    }

    fun pullDomainEvents(): List<DomainEvent> {
        return this.events
    }
}

// Clase Status
data class Status(
    val state: String,
    val description: String
)