package xyz.colmmurphy.klaassify.controllers

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.text.Text
import javafx.stage.Stage
import xyz.colmmurphy.klaassify.api.socket.SocketAPI
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

    val socket : SocketAPI = SocketAPI("http://localhost:3000")
    var loginLink : String = ""
    var userID : String = "myid"
    fun initialize() {
        loginButton.isVisible=false
        logoutButton.isVisible=false
        socket.connect()

        println("requesting token")
        socket.requestAuthToken(userID)
        responseText.text = "Error receiving response from server (is server on?)"

        socket.onEvent("authorized") {
            println("Handshake complete")
            loginButton.isVisible=false
            responseText.isVisible=true
            responseText.text = "Authorization complete, redirecting..."
            //move view to graph
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
        }
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