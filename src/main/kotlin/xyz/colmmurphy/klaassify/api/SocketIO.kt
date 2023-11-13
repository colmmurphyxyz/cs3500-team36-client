package xyz.colmmurphy.klaassify.api

class SocketIO(val uri: String) {
    private val socketHandler = SocketHandler(uri)
    init {
        socketHandler.establishConnection()
    }


}
