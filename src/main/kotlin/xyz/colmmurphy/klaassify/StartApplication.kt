// StartApplication.kt
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
import java.io.File

class StartApplication : Application() {

    override fun start(primaryStage: Stage) {
        loadDummyGraphData(15)

        primaryStage.title = "CS3500 Team 36 client"
        val root = FXMLLoader.load<Parent>(Client::class.java.classLoader.getResource("view/main-view.fxml"))
        primaryStage.scene = Scene(root, 1000.0, 1000.0)
        primaryStage.show()
    }

    companion object {
        val artistGraph = ArtistGraph(commonGenre)
        @JvmStatic
        fun main(args: Array<String>) {
            launch(StartApplication::class.java, *args)
        }

        private fun loadTestGraph() {
            val artists = hashMapOf<Int, Artist>()
            for (i in 0..20) {
                val newArtist = Artist(i.toString(), setOf<String>(), 0L, "https://example.com", 1, listOf<Image>())
                artists[i + 1] = newArtist
                artistGraph.addVertex(newArtist)
            }
            artistGraph.addEdge(artists[21]!!, artists[5]!!, 1)
            artistGraph.addEdge(artists[19]!!, artists[5]!!, 1)
            artistGraph.addEdge(artists[3]!!, artists[15]!!, 1)
            artistGraph.addEdge(artists[5]!!, artists[6]!!, 1)
            artistGraph.addEdge(artists[4]!!, artists[15]!!, 1)
            artistGraph.addEdge(artists[9]!!, artists[10]!!, 1)
            artistGraph.addEdge(artists[11]!!, artists[12]!!, 1)
            artistGraph.addEdge(artists[15]!!, artists[12]!!, 1)
            artistGraph.addEdge(artists[7]!!, artists[13]!!, 1)
            artistGraph.addEdge(artists[13]!!, artists[4]!!, 1)
            artistGraph.addEdge(artists[5]!!, artists[14]!!, 1)
            artistGraph.addEdge(artists[4]!!, artists[8]!!, 1)
            artistGraph.addEdge(artists[6]!!, artists[16]!!, 1)
            artistGraph.addEdge(artists[16]!!, artists[8]!!, 1)
            artistGraph.addEdge(artists[17]!!, artists[12]!!, 1)
            artistGraph.addEdge(artists[11]!!, artists[13]!!, 1)
            artistGraph.addEdge(artists[10]!!, artists[18]!!, 1)
            artistGraph.addEdge(artists[18]!!, artists[12]!!, 1)
            artistGraph.addEdge(artists[20]!!, artists[5]!!, 1)
            artistGraph.addEdge(artists[20]!!, artists[10]!!, 1)
            artistGraph.addEdge(artists[3]!!, artists[21]!!, 1)
            artistGraph.addEdge(artists[3]!!, artists[2]!!, 1)
            artistGraph.addEdge(artists[1]!!, artists[2]!!, 1)
            artistGraph.addEdge(artists[1]!!, artists[21]!!, 1)
            println("created graph")
            println(artistGraph)
        }

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
                artistGraph.addVertexAndCreateEdges(
                    Artist(
                    artistName,
                    genres,
                    0L,
                    "https://example.com",
                    1,
                    listOf<Image>()
                )
                )
            }

            println("created graph")
            println(artistGraph)
        }
    }
}
