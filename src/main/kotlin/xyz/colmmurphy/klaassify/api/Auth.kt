package xyz.colmmurphy.klaassify.api

import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.enums.AuthorizationScope
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest
import se.michaelthelin.spotify.requests.authorization.authorization_code.pkce.AuthorizationCodePKCERequest
import java.net.URI
import kotlin.random.Random

const val CLIENT_ID: String = "0ff2fd2a2e3c4819b57c15a1f6aea1a1"
const val SECRET: String = "2fc8c6ddc39a461b99028a3ce80589c8"
const val REDIRECT_URI = "http://localhost:8080"
var code: String = ""

fun generateCodeVerifier(length: Int=0): String {
    val l = if (length <= 0) Random.Default.nextInt(43, 129) else length
    val possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    var s = ""
    repeat(length) {
        s += possible[Random.Default.nextInt(possible.length)]
    }
    return s
}

val codeVerifier = generateCodeVerifier(64)
val codeChallenge = base64Encode(sha256(codeVerifier), urlSafe=true)

val spotifyApi = SpotifyApi.Builder()
    .setClientId(CLIENT_ID)
    .setRedirectUri(URI(REDIRECT_URI))
    .build()

fun authorizationCodeUriRequest(): AuthorizationCodeUriRequest = spotifyApi.authorizationCodePKCEUri(codeChallenge)
    .scope(
        AuthorizationScope.USER_READ_PRIVATE,
        AuthorizationScope.PLAYLIST_READ_PRIVATE,
        AuthorizationScope.USER_FOLLOW_READ,
        AuthorizationScope.USER_LIBRARY_READ,
        AuthorizationScope.USER_READ_EMAIL,
        AuthorizationScope.USER_READ_RECENTLY_PLAYED,
        AuthorizationScope.USER_TOP_READ
    )
    .show_dialog(true)
    .build()


fun authorizationCodeUri(): URI {
    val uri = authorizationCodeUriRequest().execute()
    println("login link: $uri")
    return uri
}

fun authorizationCodePKCERequest(): AuthorizationCodePKCERequest = spotifyApi.authorizationCodePKCE(code, codeVerifier)
    .build()

fun authorizationCode() {
    println("Obtaining authorization code with code=$code and codeVerifier=$codeVerifier")
    println("and code challenge $codeChallenge")
    val authorizationCodeCredentials = authorizationCodePKCERequest().execute()
    spotifyApi.accessToken = authorizationCodeCredentials.accessToken
    spotifyApi.refreshToken = authorizationCodeCredentials.refreshToken
    println("Obtained access token ${spotifyApi.accessToken} and refresh token ${spotifyApi.refreshToken}. \n" +
            "Expires in ${authorizationCodeCredentials.expiresIn}")
    val usersTopArtists = spotifyApi.usersTopArtists
        .time_range("medium_term")
        .limit(100)
        .build()
        .execute()

    println("total: ${usersTopArtists.total}")
    println(usersTopArtists.items.joinToString { it -> "${it.name}: [${it.genres.joinToString(",")}]\n" })
}