package utn.methodology.application.commands
import kotlinx.serialization.Serializable

@Serializable

data class CreateUserCommand (
    val name: String,

    val username: String,

    val email: String,

    val password: String
)

