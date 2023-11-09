package xyz.colmmurphy.klaassify.controllers

import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.fxml.FXML
import javafx.scene.web.WebEngine
import javafx.scene.web.WebView
import java.net.URI
import javafx.concurrent.Worker.State;
import xyz.colmmurphy.klaassify.api.authorizationCodeUriRequest


class SpotifyAuthController {
    @FXML
    lateinit var webView: WebView
    lateinit var webEngine: WebEngine

    fun initialize() {
        webEngine = webView.engine
        println(webEngine.userAgent)
        val uri: URI = authorizationCodeUriRequest()
        webEngine.load(uri.toString())
//        webEngine.loadWorker.stateProperty().addListener(
//            object : ChangeListener<State?>() {
//                fun changed(ov: ObservableValue<*>?, oldState: State?, newState: State) {
//                    if (newState === State.SUCCEEDED) {
//                        stage.setTitle(webEngine.location)
//                    }
//                }
//            })
        webEngine.loadWorker.stateProperty().addListener { observableValue, oldState, newState ->
            println("State change!!")
            println(observableValue.value.toString())
            println(oldState.name)
            println(newState.name)
        }
    }
}