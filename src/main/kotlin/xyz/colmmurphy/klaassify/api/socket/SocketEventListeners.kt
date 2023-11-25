// EventListeners.kt

package xyz.colmmurphy.klaassify.api.socket

import io.socket.client.Socket
import io.socket.emitter.Emitter

class SocketEventListeners(private val socket: Socket) {
    private val eventListeners = mutableMapOf<String, (String) -> Unit>()

    // Event listener
    private val onAuthorizedEvent = Emitter.Listener { args ->
        notifyEventListener("authorized", "handshake complete")
    }
    private val onLoginLink = Emitter.Listener { args ->
        val message = args[0].toString()
        notifyEventListener("login_link", message)
    }
    private val onTopArtistsResponse = Emitter.Listener { args ->
        val message = args[0].toString()
        notifyEventListener("top_artists_response", message)
    }
    private fun notifyEventListener(eventName: String, message: String) {
        eventListeners[eventName]?.invoke(message)
    }

    fun getEventListeners(): MutableMap<String, (String) -> Unit> {
        return eventListeners;
    }

    init {
        // Set up listener for authorised event
        socket.on("authorized", onAuthorizedEvent)
        socket.on("login_link", onLoginLink)
        socket.on("top_artists_response", onTopArtistsResponse)
    }
}
