package xyz.colmmurphy.klaassify.api
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.serialization.json.Json
import xyz.colmmurphy.klaassify.api.socket.SocketEventListeners
import xyz.colmmurphy.klaassify.collections.Artist

class SocketAPI(uri:String) {
    //Event listeners could definetly be spread to different class but
    // for my own dummy brain sake for now keep em in same file
    private val socket: Socket = IO.socket(uri)
    // Add socket to eventlisteners
    private val eventListeners =  SocketEventListeners(socket).getEventListeners();
    fun connect() {
        
        socket.connect()

        socket.on(Socket.EVENT_CONNECT) {
            println("Connected to server")
        }
        socket.on("connection_request_ACK") { args ->
            println("Connection Acknowledgement from server. Server response: ${args[0]}")

        }

        socket.on("top_artists_response") { data ->
            val o = Array<Artist>(20) {
                Json.decodeFromString<Artist>(data[it] as String)
            }
        }

        socket.on(Socket.EVENT_DISCONNECT) {
            println("Disconnected from server")
        }
    }

    //This is intended to initalise the current users authentication token in DB rather than actually return one
    fun requestAuthToken(userID:String){
        socket.emit("request_auth_token", userID)
    }
    fun requestTopArtist(userID:String){
        socket.emit("top_artists_request",userID)
    }
    // Register an event to event listeners
    fun onEvent(eventName: String, listener: (String) -> Unit) {
        eventListeners[eventName] = listener
    }

    fun disconnect() {
        socket.emit("disconnect")
        socket.disconnect()
    }
}