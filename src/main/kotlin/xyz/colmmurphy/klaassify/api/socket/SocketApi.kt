package xyz.colmmurphy.klaassify.api.socket

import io.socket.client.IO
import io.socket.client.Socket

    class SocketAPI(uri:String) {

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
        fun requestBenOrColmData(benOrColm:String){
            socket.emit("request_ben_or_colm_data",benOrColm)
        }
        fun logout(userID: String){
            socket.emit("logout",userID)
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
