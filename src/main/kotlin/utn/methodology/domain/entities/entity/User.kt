package utn.methodology.domain.entities.entity

import utn.methodology.domain.entities.events.DomainEvent
import utn.methodology.domain.entities.events.UserCreated

data class User(
    val uuid: Int = generateUuid(),
    val name: String,
    val username: String,
    var email: String,
    var password: String
) {
    private var events: List<DomainEvent> = mutableListOf();
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
              primitives["userName"] as String,
              primitives["email"] as String,
              primitives["password"] as String,
            );

            user.validate()
            return user
        }

        fun create(
           uuid: Int,
           name: String,
           username: String,
           email: String,
           password: String
        ): User {
            //Creación del objeto Usuario
            val user = User(
                uuid,
                name,
                username,
                email,
                password
            )

            //Registra el evento de la creación del nuevo usuario
            user.recordEvent(
                UserCreated(
                    user.uuid,
                    name,
                    username,
                    email,
                    password
                )
            )

            user.status = Status("created", "pending")

            if(name != null || username != null || email != null || password != null){
                user.status = Status("created", "still_creating")
            }

            return user
        }
    }

    private fun validate(){
        require(name.isNotBlank()) {"El nombre no puede estar ser vacío"}
        require(username.isNotBlank()) {"El nombre de usuario no puede ser vacío"}
        require(email.isNotBlank() && email.contains("@")) {"Email inválido"}
        require(password.isNotBlank() && password.length >= 8) {"La contraseña debe tener 8 o más caracteres"}
    }

    private fun recordEvent(event: DomainEvent){
        this.events = this.events.plus(event)
    }

    fun toPrimitives(): Map<String, Any?> {
        return mapOf(
            "uuid" to this.uuid,
            "name" to this.name,
            "username" to this.username,
            "email" to this.email,
            "password" to this.password
        )
    }

    fun pullDomainEvents(): List<DomainEvent> {
        return this.events;
    }

}