package com.fazlerabbikhan.pushnotifications.data

data class TokenRequest(
    val token: String
)

data class TokenResponse(
    val data: String?,
    val json: Token?
)

data class Token(
    val token: String?
)