package xyz.colmmurphy.klaassify.api

import io.socket.client.IO
import io.socket.client.Socket

class SocketApi(val uri: String) {
    private val socket = IO.socket(uri)
    fun getSocket(): Socket = socket

    init {
        socket.on("echo_response") { data ->
            println("Received echo_response: ${data.joinToString("+")}")
        }
        socket.on("artist_info_response") { data ->
            println("Received artist_info_response: ${data.joinToString("+")}")
        }
    }

    fun establishConnection() {
        socket.connect()
    }

    fun closeConnection() {
        socket.disconnect()
    }

    fun echo(vararg args: String) {
        socket.emit("echo", args)
    }

    fun getArtistInfo(artistId: String) {
        socket.emit("artist_info", artistId)
    }
}