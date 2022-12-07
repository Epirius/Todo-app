package dev.felixkaasa.todo.plugins

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import java.util.*


fun Application.configureSecurity() {


    authentication {
        val secret = System.getenv("JWT_SECRET")
        val issuer = System.getenv("JWT_ISSUER")
        val jwtAudience = System.getenv("JWT_AUDIENCE")
        val jwtAlgorithm = Algorithm.HMAC256(secret)

        jwt ("auth-jwt"){
            verifier(
                JWT
                    .require(jwtAlgorithm)
                    .withAudience(jwtAudience)
                    .withIssuer(issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
            }
        }
    }
}

fun createToken(email: String): String {
    val secret = System.getenv("JWT_SECRET")
    val issuer = System.getenv("JWT_ISSUER")
    val jwtAudience = System.getenv("JWT_AUDIENCE")
    val jwtAlgorithm = Algorithm.HMAC256(secret)

    return JWT.create()
        .withAudience(jwtAudience)
        .withIssuer(issuer)
        .withClaim("email", email)
        .withExpiresAt(Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7))) // 7 days
        .sign(jwtAlgorithm)
}