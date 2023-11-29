package xyz.colmmurphy.klaassify

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import xyz.colmmurphy.klaassify.collections.Artist
import xyz.colmmurphy.klaassify.collections.ArtistGraph
import xyz.colmmurphy.klaassify.collections.commonGenre
import xyz.colmmurphy.klaassify.collections.twoCommonGenres
import java.io.File

/**
 * Klaassify Client
 */
class Client : Application() {
    /**
     * Creates a displays a scene with from main-view.fxml
     */
    override fun start(primaryStage: Stage) {
        primaryStage.title = "CS3500 Team 36 client"
        // temporarily change main scene to graph-view with dummy graph data
        loadDummyGraphData(15)
        val root = FXMLLoader.load<Parent>(Client::class.java.classLoader.getResource("view/graph-view.fxml"))
//        val root = FXMLLoader.load<Parent>(Client::class.java.classLoader.getResource("view/main-view.fxml"))
        primaryStage.scene = Scene(root, 1000.0, 1000.0)
        primaryStage.show()
    }

    fun main(args: Array<String>) {
        // * is the spread operator similar to the ... operator in JS
        // necessary here as the main function accepts an array of Strings as params and the JavaFX launch method
        // requires `vararg String` as its parameter type
        launch(*args)
    }

    /**
     * Anything inside a companion object is considered static, can be accessed without an instance of the Client class
     * e.g val g = Client.artistGraph
     * Global variables can be stored here
     */
    companion object {
        val artistGraph = ArtistGraph(commonGenre)
        val serverUrl = System.getenv("SERVER_URL")

        /**
         * Load the artist information specified in the artists.txt resource file
         * and add the information to the artistGraph
         */
        private fun loadDummyGraphData(limit: Int=50) {
            val inFile = File(Client::class.java.classLoader.getResource("artists.txt")!!.toURI())
            var linesCounted = 0
            inFile.forEachLine { line ->
                linesCounted++
                if (linesCounted > limit) return@forEachLine
                val artistName = line.substringBefore(":")
                val genres = line
                    .substringAfter("[")
                    .substringBefore("]")
                    .split(", ").toSet()
                artistGraph.addVertexAndCreateEdges(Artist(
                    artistName,
                    genres,
                    0L,
                    "https://example.com",
                    1
                ))
            }
            println("created graph")
            println(artistGraph)
        }
    }
}