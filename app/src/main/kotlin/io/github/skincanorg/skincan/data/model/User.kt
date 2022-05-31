package io.github.skincanorg.skincan.data.model

data class MiniUser(
    val username: String,
    val password: String,
)

data class User(
    val username: String,
    val email: String,
    val password: String,
    val date: String,  // Zulu time
)
