package xyz.colmmurphy.team36.api

import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.enums.AuthorizationScope
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest
import java.net.URI

const val CLIENT_ID: String = "0ff2fd2a2e3c4819b57c15a1f6aea1a1"
const val SECRET: String = "2fc8c6ddc39a461b99028a3ce80589c8"
const val REDIRECT_URI = "http://localhost/callback"
val SCOPES = arrayOf<AuthorizationScope>(
    AuthorizationScope.USER_READ_PRIVATE,
    AuthorizationScope.PLAYLIST_READ_PRIVATE)

val spotifyApi: SpotifyApi = SpotifyApi.Builder()
    .setClientId(CLIENT_ID)
    .setClientSecret(SECRET)
    .setRedirectUri(URI(REDIRECT_URI))
    .build()

fun authorizationCodeUriRequest(): AuthorizationCodeUriRequest {
    return spotifyApi.authorizationCodeUri()
        .scope(*SCOPES)
        .build()
}

//fun authorizationCodeUri() {
//    val uri: URI = authorizationCodeUriRequest().execute()
//    println(uri.toString())
//}