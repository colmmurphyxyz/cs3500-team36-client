package xyz.colmmurphy.klaassify.api

import io.socket.client.IO
import io.socket.client.Socket
import java.net.URI

class SocketHandler {
    private val uri: String
    fun getUri(): String = uri

    private val socket: Socket
    fun getSocket(): Socket = socket

    constructor(uri: String) {
        this.uri = uri
        socket = IO.socket(uri)
    }
    constructor(uri: URI) {
        this.uri = uri.toString()
        socket = IO.socket(uri)
    }

    fun establishConnection() {
        socket.connect()
    }

    fun closeConnection() {
        socket.disconnect()
    }
}
