package xyz.colmmurphy.klaassify

import xyz.colmmurphy.klaassify.api.SocketApi

/**
 * The entrypoint of the application
 * launches the JavaFX client with the supplied args
 * @param args arguments to pass to JavaFX
 */
fun main(args: Array<String>) {
//    val code = authorizationCodeUriRequest()
//    println(code)
//    authorizationCodeUri()
    Client().main(args)
}