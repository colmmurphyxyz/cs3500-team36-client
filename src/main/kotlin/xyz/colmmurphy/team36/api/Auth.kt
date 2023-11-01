package xyz.colmmurphy.team36.api

import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest
import java.net.URI

const val CLIENT_ID: String = "0ff2fd2a2e3c4819b57c15a1f6aea1a1"
const val SECRET: String = "2fc8c6ddc39a461b99028a3ce80589c8"

val spotifyApi: SpotifyApi = SpotifyApi.Builder()
    .setClientId(CLIENT_ID)
    .setClientSecret(SECRET)
    .setRedirectUri(URI("https://colmmurphy.xyz/spotifyauth"))
    .build()

fun authorizationCodeUriRequest(): AuthorizationCodeUriRequest {
    return spotifyApi.authorizationCodeUri().build()
}

//fun authorizationCodeUri() {
//    val uri: URI = authorizationCodeUriRequest().execute()
//    println(uri.toString())
//}