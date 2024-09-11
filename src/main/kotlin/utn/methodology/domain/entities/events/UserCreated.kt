package utn.methodology.domain.entities.events

import utn.methodology.domain.entities.events.DomainEvent

data class UserCreated(
    val uuid: Int,
    val name: String,
    val username: String,
    var email: String,
    var password: String
) : DomainEvent {
    override  val eventName: String = "user.created"
}
