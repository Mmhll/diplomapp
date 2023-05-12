package com.ggg.gggapp.utils

import com.auth0.android.jwt.JWT

class JWTParser(val token: String) {
    private var jwt = JWT(token)

    fun getId(): Long{
        return jwt.claims["user_id"]?.asLong()!!
    }

    fun getClaimString(value: String): String{
        return jwt.claims[value]?.asString()!!
    }

    fun getSubject(): String{
        return jwt.subject!!
    }
}