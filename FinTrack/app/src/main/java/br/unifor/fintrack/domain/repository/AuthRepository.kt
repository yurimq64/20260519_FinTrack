package br.unifor.fintrack.domain.repository

import br.unifor.fintrack.domain.model.AuthUser

interface AuthRepository {

    fun currentUserOrNull(): AuthUser?

    suspend fun signUp(
        fullName:String,
        email: String,
        password: String
    ): AuthUser

    suspend fun signIn(
        email: String,
        password: String
    ): AuthUser

    suspend fun signOut()

}