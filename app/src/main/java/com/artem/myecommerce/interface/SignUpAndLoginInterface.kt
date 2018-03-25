package com.artem.myecommerce.`interface`

interface SignUpAndLoginInterface {
    fun finishedSignUp()
    fun finishedLogin(accessToken: String)
    fun updateAccessToken(accessToken: String)
    fun getCurrAccessToken(): String
}