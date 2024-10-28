package utn.methodology.application.models

data class CreateUserCommand(
    val username: String,
    val email: String,
    val password: String
)