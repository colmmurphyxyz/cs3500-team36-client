package xyz.colmmurphy.klaassify.controllers

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.text.Text
import javafx.stage.Stage
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArrayBuilder
import xyz.colmmurphy.klaassify.StartApplication
import xyz.colmmurphy.klaassify.api.socket.SocketAPI
import xyz.colmmurphy.klaassify.collections.Artist
import xyz.colmmurphy.klaassify.collections.ArtistJsonEntry
import java.awt.Desktop
import java.net.URI

/**
 * Controller for the main view, or the landing page to the application
 */
class Controller {
    @FXML
    lateinit var loginButton: Button
    @FXML
    lateinit var logoutButton: Button
    @FXML
    lateinit var title: Text
    @FXML
    lateinit var responseText: Text
    @FXML
    lateinit var redirectButton: Button

    val socket : SocketAPI = SocketAPI("http://localhost:3000")
    var loginLink : String = ""
    var userID : String = "myid"
    fun initialize() {

        loginButton.isVisible=false
        logoutButton.isVisible=false
        socket.connect()
        socket.requestBenOrColmData("ben")
        println("requesting token")
        socket.requestAuthToken(userID)
        responseText.text = "Error receiving response from server (is server on?)"

        socket.onEvent("authorized") {
            println("Handshake complete")
            loginButton.isVisible=false
            responseText.isVisible=true
            responseText.text = "Authorization complete, redirecting..."
            redirectButton.isVisible = true
            socket.requestTopArtist(userID)
            logoutButton.isVisible=true
        }

        socket.onEvent("login_link") { eventData ->
            loginLink=eventData
            loginButton.isVisible=true
            responseText.isVisible=false
        }

        socket.onEvent("top_artists_response"){ eventData ->
            println("Top artists: $eventData")
            val jsonString = eventData.substringAfter("\"items\":").substringBeforeLast('}')
            val artists = Json.decodeFromString<List<ArtistJsonEntry>>(jsonString)
            artists.forEach {
                val a = it.toArtist()
                StartApplication.artistGraph.addVertexAndCreateEdges(a)
            }
            for (a in StartApplication.artistGraph.vertices) {
                println("degree=${StartApplication.artistGraph.degree(a)}")
                if (StartApplication.artistGraph.degree(a) == 0) {
                    StartApplication.artistGraph.removeVertex(a)
                }
            }
            for (a in StartApplication.artistGraph.vertices) {
                var noConnections = true
                for (b in StartApplication.artistGraph.vertices) {
                    if (a == b) continue
                    if ((a.genres intersect b.genres).isNotEmpty()) {
                        noConnections = false
                    }
                }
                if (noConnections) {
                    println("removing ${a.name}")
                    StartApplication.artistGraph.removeVertex(a)
                }
            }
            redirectButton.isVisible = true
        }

        socket.onEvent("response_ben_or_colm_data"){ eventData ->
            println("BenOrColmData Data: $eventData")
        }
    }

    fun onRedirectButtonClick() {
        println("redirecting")
        val root: Parent = FXMLLoader.load<Parent>(
            this::class.java.classLoader.getResource("view/graph-view.fxml")
        )

        val window: Stage = loginButton.scene.window as Stage
        window.scene = Scene(root, 1400.0, 1000.0)
    }

    fun onLoginButtonClick() {
        openBrowser(loginLink)
    }
    fun onLogoutButtonClick() {
        socket.logout(userID)
        //REDIRECT TO THIS VIEW ? AND RELOAD CONTROLLER? IDK WHAT BEST PRACTISE IS
    }
    private fun openBrowser(url: String){
        val os = System.getProperty("os.name").lowercase()
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                //IF linux based system (ick)
                if (os.contains("nix") || os.contains("nux") || os.contains("aix")){
                    val processBuilder = ProcessBuilder("xdg-open", url)
                    val process = processBuilder.start()
                    val exitCode = process.waitFor()
                    if(exitCode !=0){
                        print("Error Opening url")
                        }
                    }
                val uri = URI(url)
                Desktop.getDesktop().browse(uri)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            println("Desktop browsing is not supported on this platform.")
        }
    }
}