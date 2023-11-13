package xyz.colmmurphy.klaassify.controllers

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.text.Text
import javafx.stage.Stage

/**
 * Controller for the main view, or the landing page to the application
 */
class Controller {
    @FXML
    lateinit var button: Button
    @FXML
    lateinit var title: Text

    fun initialize() {

    }

    /**
     * On pressing the button, this method is called, changing the active scene to the one
     * defined in spotify-redirect.fxml
     */
    fun onButtonClick() {
        val root: Parent = FXMLLoader.load<Parent>(
            this::class.java.classLoader.getResource("view/spotify-redirect.fxml")
        )

        val window: Stage = button.scene.window as Stage
        window.scene = Scene(root, 1000.0, 1000.0)
    }
}