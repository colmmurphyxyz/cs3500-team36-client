package xyz.colmmurphy.team36

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage


class Client : Application() {
    override fun start(primaryStage: Stage) {
        primaryStage.title = "CS3500 Team 36 client"
        val root = FXMLLoader.load<Parent>(Client::class.java.classLoader.getResource("view/main-view.fxml"))
        primaryStage.scene = Scene(root, 1000.0, 1000.0)
        primaryStage.show()
    }

    fun main(args: Array<String>) {
        // * is the spread operator similar to the ... operator in JS
        // necessary here as the main function accepts an array of Strings as params and the JavaFX launch method
        // requires `vararg String` as its parameter type
        launch(*args)
    }
}