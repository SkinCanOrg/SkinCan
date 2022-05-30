package io.github.skincanorg.skincan.data.model

data class ApiResponse(
    val message: String?,
    val data: Any,  // TODO
)

data class LoginResponse(
    val message: String?,
    val data: User,  // TODO
)
