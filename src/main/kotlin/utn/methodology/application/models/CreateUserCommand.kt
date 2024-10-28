package com.example.models

data class CreateUserCommand(
    val username: String,
    val email: String,
    val password: String
)