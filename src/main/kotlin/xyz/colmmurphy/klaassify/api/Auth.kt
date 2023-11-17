package xyz.colmmurphy.klaassify.api

import javafx.application.Application
import javafx.scene.image.Image
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.enums.AuthorizationScope
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest
import se.michaelthelin.spotify.requests.authorization.authorization_code.pkce.AuthorizationCodePKCERequest
import xyz.colmmurphy.klaassify.Client
import xyz.colmmurphy.klaassify.collections.ArtistImage
import java.net.URI
import kotlin.random.Random

const val CLIENT_ID: String = "0ff2fd2a2e3c4819b57c15a1f6aea1a1"
const val SECRET: String = "2fc8c6ddc39a461b99028a3ce80589c8"
const val REDIRECT_URI = "http://localhost:8080"
var code: String = ""

/**
 * generates a pseodo-random string of characters and digits to be used as a code verifier for PKCE Auth purposes
 */
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

/**
 * Generates a request for a login link from Spotify with the given scopes
 * @return AuthorizationCodeUriRequest for PKCE authorization
 */
fun authorizationCodeUriRequest(): AuthorizationCodeUriRequest = spotifyApi.authorizationCodePKCEUri(codeChallenge)
    .scope(
        AuthorizationScope.USER_READ_PRIVATE,
        AuthorizationScope.PLAYLIST_READ_PRIVATE,
        AuthorizationScope.USER_FOLLOW_READ,
        AuthorizationScope.USER_LIBRARY_READ,
        AuthorizationScope.USER_READ_EMAIL,
        AuthorizationScope.USER_READ_RECENTLY_PLAYED,
        AuthorizationScope.USER_TOP_READ,
    )
    .show_dialog(true)
    .build()


/**
 * Executes the authorizationUriRequest and receives a login link from Spotify
 * @return Authorization login page URI link
 */
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
    // example code to get a user's top artists and crete a graph from it
    // doesn't belong in this function scope
//    val usersTopArtists = spotifyApi.usersTopArtists
//        .time_range("medium_term")
//        .limit(100)
//        .build()
//        .execute()
//
//    println("total: ${usersTopArtists.total}")
//    println(usersTopArtists.items.joinToString(separator="\n") { it -> "${it.name}: [${it.genres.joinToString()}]" })
//    usersTopArtists.items.forEach {
//        val newArtist = xyz.colmmurphy.klaassify.collections.Artist(it.name,
//            it.genres.toSet(),
//            it.followers.total.toLong(),
//            it.uri,
//            it.popularity,
//            listOf<Image>()
//        )
//        Client.artistGraph.addVertexAndCreateEdges(newArtist)
//    }
//    println("created graph")
//    println(Client.artistGraph)
}