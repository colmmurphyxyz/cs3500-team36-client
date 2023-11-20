package xyz.colmmurphy.klaassify.api
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter

class ClientConnect() {

    //Event listeners could definetly be spread to different class but
    // for my own dummy brain sake for now keep em in same file
    private val socket: Socket = IO.socket("http://localhost:3000")
    // Add a list to store event listeners
    private val eventListeners = mutableMapOf<String, (String) -> Unit>()


    // Event listener
    private val onAuthorizedEvent = Emitter.Listener { args ->
        notifyEventListener("authorized", "handshake complete")
    }
    private val onLoginLink = Emitter.Listener { args ->
        val message = args[0].toString()
        notifyEventListener("login_link", message)
    }

    init {
        // Set up listener for authorised event
        socket.on("authorized", onAuthorizedEvent)
        socket.on("login_link", onLoginLink)

    }

    // Function to send a message
    fun sendMessage(message: String) {
        socket.emit("clientMessage", message)
    }
    //This is intended to initalise the current users authentication token in DB rather than actually return one
    fun requestAuthToken(userID:String){
        socket.emit("request_auth_token", userID)
    }

    // Register an event to event listeners
    fun onEvent(eventName: String, listener: (String) -> Unit) {
        eventListeners[eventName] = listener
    }

    // Notify an event listener with message
    private fun notifyEventListener(eventName: String, message: String) {
        eventListeners[eventName]?.invoke(message)
    }


    fun connect() {
        
        socket.connect()

        socket.on(Socket.EVENT_CONNECT) {
            println("Connected to server")
        }
        socket.on("connection_request_ACK") { args ->
            println("Connection Acknowledgement from server. Server response: ${args[0]}")

        }

        socket.on(Socket.EVENT_DISCONNECT) {
            println("Disconnected from server")
        }
    }
    fun disconnect() {
        socket.emit("disconnect")
        socket.disconnect()
    }
}