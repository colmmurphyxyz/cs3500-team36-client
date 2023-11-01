package xyz.colmmurphy.team36.controllers

import javafx.fxml.FXML
import javafx.scene.web.WebEngine
import javafx.scene.web.WebView
import xyz.colmmurphy.team36.api.authorizationCodeUriRequest
import java.net.URI

class SpotifyAuthController {
    @FXML
    lateinit var webView: WebView
    lateinit var webEngine: WebEngine

    fun initialize() {
        webEngine = webView.engine
        println(webEngine.userAgent)
        val uri: URI = authorizationCodeUriRequest().execute()
        webView.engine.load(uri.toString())
    }
}