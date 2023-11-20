// StartApplication.kt
package xyz.colmmurphy.klaassify

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

class StartApplication : Application() {

    override fun start(primaryStage: Stage) {
        primaryStage.title = "CS3500 Team 36 client"
        val root = FXMLLoader.load<Parent>(Client::class.java.classLoader.getResource("view/main-view.fxml"))
        primaryStage.scene = Scene(root, 1000.0, 1000.0)
        primaryStage.show()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(StartApplication::class.java, *args)
        }
    }
}
