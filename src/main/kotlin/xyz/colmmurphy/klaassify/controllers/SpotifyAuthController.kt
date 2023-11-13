package xyz.colmmurphy.klaassify.controllers

import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.web.WebEngine
import javafx.scene.web.WebView
import javafx.stage.Stage
import java.net.URI
import xyz.colmmurphy.klaassify.api.REDIRECT_URI
import xyz.colmmurphy.klaassify.api.authorizationCode
import xyz.colmmurphy.klaassify.api.code
import xyz.colmmurphy.klaassify.api.authorizationCodeUri

class SpotifyAuthController {
    @FXML
    lateinit var webView: WebView
    lateinit var webEngine: WebEngine

    fun initialize() {
        webEngine = webView.engine
        println(webEngine.userAgent)
        val uri: URI = authorizationCodeUri()
        webEngine.load(uri.toString())
        // add a listener to run a function every time the window.loaction property changes
        webEngine.locationProperty().addListener(ChangeListener<String>() { observableValue, oldValue, newValue ->
            println("redirected from $oldValue to $newValue")
            // if spotify login was successful, spotify will redirect us to REDIRECT_URI
            // with the special code in the URL params
            if (!newValue.startsWith(REDIRECT_URI)) return@ChangeListener
            // we have the special code, we save this, and can use it to generate an access token
            // set the code variable in the AuthKt class and use it to obtain an access token
            code = newValue.substringAfter('=')
            // obtain access and refresh tokens, then change the scene
            authorizationCode()

            val root: Parent = FXMLLoader.load<Parent>(
                this::class.java.classLoader.getResource("view/graph-view.fxml")
            )

            val window: Stage = webView.scene.window as Stage
            window.scene = Scene(root, 1000.0, 1000.0)
        })
    }
}