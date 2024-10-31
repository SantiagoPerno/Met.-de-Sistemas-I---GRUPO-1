package utn.methodology.application.models

data class FollowRequest (
    val userId: String,
    val followedUserId: String
)