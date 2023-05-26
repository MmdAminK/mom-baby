package com.app.mombaby.models.user

data class User(
    val firstName: String? = null,
    val lastName: String? = null,
    val phoneNumber: String? = null,
    val email: String? = null,
    val age: Int? = null,
    val job: String? = null,
    val childNum: Int? = null,
) {
}