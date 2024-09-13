package utn.methodology.domain.entities.commands

data class createUserCommand(
    val name: String,
    val username: String,
    val email: String,
    val password: String
)
