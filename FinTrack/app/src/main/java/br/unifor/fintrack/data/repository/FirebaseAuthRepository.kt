package br.unifor.fintrack.data.repository

import br.unifor.fintrack.domain.model.AuthUser
import br.unifor.fintrack.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
): AuthRepository {

    override fun currentUserOrNull(): AuthUser? = firebaseAuth.currentUser?.toAuthUser()

    override suspend fun signUp(
        fullName: String,
        email: String,
        password: String
    ): AuthUser {
        try{
            val authResult = firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .await()

            val user = authResult.user
                ?: throw Exception("Erro ao criar o usuário.")

            val profileUpdate = UserProfileChangeRequest.Builder()
                .setDisplayName(fullName)
                .build()

            user.updateProfile(profileUpdate).await()
            user.reload().await()

            return firebaseAuth.currentUser?.toAuthUser() ?: throw Exception("Erro ao recuperar o usuário")

        } catch (e: Exception){
            throw Exception(translateError(error = e, isSignUp = true ))
        }
    }

    override suspend fun signIn(
        email: String,
        password: String
    ): AuthUser {
        try {
            val authResult = firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .await()
            return authResult.user?.toAuthUser() ?: throw Exception("Erro ao autenticar.")
        } catch (e: Exception){
            throw Exception(translateError(error = e, isSignUp = false ))
        }
    }

    override suspend fun signOut() {
        TODO("Not yet implemented")
    }

    private fun FirebaseUser.toAuthUser(): AuthUser {
        return AuthUser(
            uid = uid,
            email = email,
            displayName = displayName
        )
    }

    private fun translateError(
        error: Throwable,
        isSignUp: Boolean
    ): String{
        return when(error) {
            is FirebaseAuthWeakPasswordException -> "A senha é muito fraca. Use no mínimo 6 caracteres"
            is FirebaseAuthUserCollisionException -> "Já existe uma conta com este endereço de e-mail"
            is FirebaseAuthInvalidUserException -> "Usuário não encontrado"
            is FirebaseAuthInvalidCredentialsException -> if(isSignUp) "E-mail é inválido" else "Endereço de e-mail ou senha invpalidos"
            else -> error.message ?: "Erro de autenticação. tente novamente"
        }
    }

}