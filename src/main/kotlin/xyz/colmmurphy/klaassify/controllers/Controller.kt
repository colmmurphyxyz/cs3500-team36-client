package xyz.colmmurphy.klaassify.controllers

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.text.Text
import javafx.stage.Stage
import xyz.colmmurphy.klaassify.api.ClientConnect
import java.awt.Desktop
import java.net.URI

/**
 * Controller for the main view, or the landing page to the application
 */
class Controller {
    @FXML
    lateinit var button: Button
    @FXML
    lateinit var title: Text

    val socket : ClientConnect = ClientConnect()
    public var loginLink : String = "";
    var userID : String = "myid"
    fun initialize() {
        button.isVisible=false
        socket.connect()
        println("requesting token")
        socket.requestAuthToken(userID)

        socket.onEvent("authorized") {
            println("Handshake complete")
            //move view to graph
        }
        socket.onEvent("login_link") { eventData ->
            button.isVisible=true
            loginLink=eventData
        }
    }

    /**
     * On pressing the button, this method is called, changing the active scene to the one
     * defined in spotify-redirect.fxml
     */
    fun onButtonClick() {
        openBrowser(loginLink)
    }

    private fun openBrowser(url: String){

        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
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