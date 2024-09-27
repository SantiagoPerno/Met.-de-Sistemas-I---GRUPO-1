package utn.methodology.application.actions

import utn.methodology.application.commandhandlers.SearchUserHandler
import utn.methodology.application.queries.SearchUserQuery
import utn.methodology.application.dtos.UserDTO

class SearchUserAction(private val handler: SearchUserHandler) {
    fun execute(query: SearchUserQuery): UserDTO? {
        return handler.handle(query)
    }
}
