package xyz.colmmurphy.klaassify.api

import java.net.URI

const val CLIENT_ID: String = "0ff2fd2a2e3c4819b57c15a1f6aea1a1"
const val SECRET: String = "2fc8c6ddc39a461b99028a3ce80589c8"
const val REDIRECT_URI = "http://localhost/callback"

fun authorizationCodeUriRequest(): URI {
    return URI("https://colmmurphy.xyz")
}

//fun authorizationCodeUri() {
//    val uri: URI = authorizationCodeUriRequest().execute()
//    println(uri.toString())
//}