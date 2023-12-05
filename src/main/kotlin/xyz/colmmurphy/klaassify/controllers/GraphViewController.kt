package xyz.colmmurphy.klaassify.controllers

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.text.Text
import javafx.stage.Stage
import xyz.colmmurphy.klaassify.StartApplication
import xyz.colmmurphy.klaassify.api.socket.SocketAPI
import xyz.colmmurphy.klaassify.collections.Artist
import java.util.*
import kotlin.concurrent.fixedRateTimer
import kotlin.math.*
import kotlin.random.Random


class GraphViewController {
    @FXML
    lateinit var title: Text
    @FXML
    lateinit var subtitle: Text
    @FXML
    lateinit var canvas: Canvas
    @FXML
    lateinit var resetButton: Button
    @FXML
    lateinit var benDataButton: Button
    @FXML
    lateinit var logOutButton: Button
    @FXML
    lateinit var colmDataButton: Button
    @FXML
    lateinit var idealEdgeLengthLabel: Label
    @FXML
    lateinit var idealEdgeLengthField: TextField
    @FXML
    lateinit var repulsionConstantField: TextField
    @FXML
    lateinit var attractionConstantField: TextField
    @FXML
    lateinit var coolingFactorField: TextField
    @FXML
    lateinit var numberIterationsField: Text
    @FXML
    lateinit var selectedArtistField: Text
    @FXML
    lateinit var selectedArtistDegreeField: Text
    @FXML
    lateinit var relatedArtistsField: Text

    private val graph = StartApplication.artistGraph
    private val userID = "myid"
    object eadesSpringEmbedder {
        private val graph = StartApplication.artistGraph
        var canvasWidth = 800.0
        var canvasHeight = 800.0

        var idealLength = 50.0
        var cRep = 2.0
        var cAttr = 1.0
        var epsilon = 0.1
        var coolingFactor = 0.99
        private var delta = 1.0
        var iterationsDone = 0

        var timer: Timer? = null

        var positions: MutableMap<Artist, DoubleArray> = mutableMapOf(
            *graph.vertices.map {
                it to (doubleArrayOf(0.0, 0.0))
            }.toTypedArray()
        )

        private var forces: MutableMap<Artist, DoubleArray> = mutableMapOf(
            *graph.vertices.map {
                it to (doubleArrayOf(0.0, 0.0))
            }.toTypedArray()
        )

        private val random = Random(System.currentTimeMillis())
        /**
         * Reset all variables. initialize positions map to random coordinates
         */
        fun reset() {
            delta = 1.0
            iterationsDone = 0
            positions = hashMapOf(
                *graph.vertices.map {
                    it to doubleArrayOf(random.nextDouble() * 800, random.nextDouble() * 800)
                }.toTypedArray()
            )
            forces = hashMapOf(
                *graph.vertices.map {
                    it to doubleArrayOf(0.0, 0.0)
                }.toTypedArray()
            )
            timer?.cancel()
        }

        fun getMaxForce(): Double {
            var maxForce = 0.0
            for (i in forces.values) {
                if (abs(i[0]) > abs(maxForce)) maxForce = i[0]
                if (abs(i[1]) > abs(maxForce)) maxForce = i[1]
            }
            return maxForce
        }

        fun fattr(u: Artist, v: Artist): DoubleArray {
            val pu = positions[u]!!
            val pv = positions[v]!!
            val dx = pv[0] - pu[0]
            val dy = pv[1] - pu[1]
            // displacement vector = [ dx dy ]
            val distance = sqrt( (dx * dx) + (dy * dy) )
            val force = cAttr * log2(distance / idealLength)
            return doubleArrayOf(
                force * (dx / distance),
                force * (dy / distance)
            )
        }

        fun frep(u: Artist, v: Artist): DoubleArray {
            val pu = positions[u]!!
            val pv = positions[v]!!
            val dx = pv[0] - pu[0]
            val dy = pv[1] - pu[1]
            // displacement (column) vector = [ dx dy ]
            val distance = sqrt( (dx * dx) + (dy * dy) )
            val force = cRep / (distance * distance)
            return doubleArrayOf(force * (dx / distance), force * (dy / distance))
        }


        fun doOneIteration() {
//            println("on iteration $iterationsDone")
            // update forces
            for (u in graph.vertices) {
                val sumForcesRep = doubleArrayOf(0.0, 0.0)
                for (v in graph.vertices) {
                    if (u == v) continue
                    val (fx, fy) = frep(u, v)
                    sumForcesRep[0] += fx
                    sumForcesRep[1] += fy
                }
                forces[u]!![0] = sumForcesRep[0]
                forces[u]!![1] = sumForcesRep[1]
                val sumForcesAttr = doubleArrayOf(0.0, 0.0)
                for (e in graph.getEdges(u)) {
                    val v = e.opposite(u) as Artist
                    if (u == v) continue
                    val (fx, fy) = fattr(u, v)
                    val (frx, fry) = frep(u, v)
                    sumForcesAttr[0] += fx - frx
                    sumForcesAttr[1] += fy - fry
                }
                forces[u]!![0] += forces[u]!![0] + sumForcesAttr[0]
                forces[u]!![1] += forces[u]!![1] + sumForcesAttr[1]
            }
            // update positions
            for (u in graph.vertices) {
//                positions[u] = positions[u]!! + delta * forces[u]
                positions[u]!![0] += delta * forces[u]!![0]
                positions[u]!![1] += delta * forces[u]!![1]
            }
            // update delta
            delta *= coolingFactor
            iterationsDone += 1
        }
    }

    private var mostRecentPositions: Map<Artist, DoubleArray> = mapOf()

    private fun drawGraph(p: MutableMap<Artist, DoubleArray>) {
        val drawCoords = mutableMapOf<Artist, DoubleArray>()
        // scale coords

        // translate all coordinates such that the smallest x coordinate is 50 and the smallest y coordinate is 50
        var minX = Double.MAX_VALUE
        var minY = Double.MAX_VALUE
        for (i in p.values) {
            minX = min(minX, i[0])
            minY = min(minY, i[1])
        }
        val xTrans = 10 - minX
        val yTrans = 10 - minY
        p.forEach { (artist, coords) ->
            drawCoords[artist] = doubleArrayOf(coords[0] + xTrans, coords[1] + yTrans)
        }
        var maxX = 0.0
        var maxY = 0.0
        for (i in drawCoords.values) {
            maxX = max(maxX, i[0])
            maxY = max(maxY, i[1])
        }
        val scaleX = (canvas.width - 50.0) / maxX
        val scaleY = (canvas.height - 50.0) / maxY
        drawCoords.forEach { (artist, coords) ->
            drawCoords[artist]!![0] *= scaleX
            drawCoords[artist]!![1] *= scaleY
        }

        mostRecentPositions = drawCoords

        // draw edges
        val gc = canvas.graphicsContext2D
        gc.clearRect(0.0, 0.0, canvas.width, canvas.height)
        gc.stroke()
        // draw edges
        gc.stroke = Color.BLACK
        for (e in graph.edges) {
            val pu = drawCoords[e.v1 as Artist]!!
            val pv = drawCoords[e.v2 as Artist]!!
            gc.strokeLine(pu[0], pu[1], pv[0], pv[1])
        }
        // draw vertices
        gc.fill = Color.GREENYELLOW
        for (node in drawCoords.keys) {
            val d = drawCoords[node]!!
            gc.fillOval(d[0] - 10.0, d[1] - 10.0, 20.0, 20.0)
        }
        gc.stroke()
    }

    private fun renderGraph(): Timer {
        eadesSpringEmbedder.reset()
        val k = 2000
        return fixedRateTimer("graph render", period = 60L) {
            if (eadesSpringEmbedder.iterationsDone < k) {
                eadesSpringEmbedder.doOneIteration()
                numberIterationsField.text = (eadesSpringEmbedder.iterationsDone + 1).toString()
                drawGraph(eadesSpringEmbedder.positions)
            } else {
                this.cancel()
            }
            if (abs(eadesSpringEmbedder.getMaxForce()) < eadesSpringEmbedder.epsilon) {
                this.cancel()
            }
        }
    }
//    private val isDouble(tf: TextField): Boolean = tf.text.matches(Regex("""^\d+(\.\d+)?$"""))
    private val isDouble: (TextField) -> Boolean =  { it.text.matches(Regex("""^\d+(\.\d+)?$""")) }
    private fun validate(tf: TextField, predicate: (TextField) -> Boolean): Boolean {
        if (predicate(tf)) {
            return true
        }
        tf.text = "Must be a positive number"
        return false
    }

    fun onResetButtonClick() {
        println("resetting")
        if (validate(idealEdgeLengthField, isDouble)) eadesSpringEmbedder.idealLength = idealEdgeLengthField.text.toDouble()
        if (validate(repulsionConstantField, isDouble)) eadesSpringEmbedder.cRep = repulsionConstantField.text.toDouble()
        if (validate(attractionConstantField, isDouble)) eadesSpringEmbedder.cAttr = attractionConstantField.text.toDouble()
        if (validate(coolingFactorField, isDouble)) {
            val cf = coolingFactorField.text.toDouble()
            if (cf >= 1.0 || cf <= 0.0) {
                coolingFactorField.text = "Must be a number between 0 and 1"
            } else {
                eadesSpringEmbedder.coolingFactor = coolingFactorField.text.toDouble()
            }
        }

        eadesSpringEmbedder.reset()
        eadesSpringEmbedder.timer = renderGraph()
    }
    fun logOut(){
        println("Logout")
        val socket : SocketAPI = SocketAPI("http://localhost:3000")
        socket.connect()
        socket.logout(userID)
        val root: Parent = FXMLLoader.load<Parent>(
            this::class.java.classLoader.getResource("view/main-view.fxml")
        )

        val window: Stage = logOutButton.scene.window as Stage
        window.scene = Scene(root, 1000.0, 1000.0)
    }
    fun initialize() {
        eadesSpringEmbedder.canvasWidth = canvas.width
        eadesSpringEmbedder.canvasHeight = canvas.height
        idealEdgeLengthField.text = eadesSpringEmbedder.idealLength.toString()
        repulsionConstantField.text = eadesSpringEmbedder.cRep.toString()
        attractionConstantField.text = eadesSpringEmbedder.cAttr.toString()
        coolingFactorField.text = eadesSpringEmbedder.coolingFactor.toString()
        renderGraph()
        canvas.addEventFilter(MouseEvent.MOUSE_MOVED) { it ->
            var selectedArtist: Artist? = null
            for ((artist, pos) in mostRecentPositions.entries) {
                if (((it.x - pos[0]) * (it.x - pos[0])) + ((it.y - pos[1]) * (it.y - pos[1])) < 30 * 30) {
                    selectedArtist = artist
                }
            }
            if (selectedArtist != null) {
                selectedArtistField.text = selectedArtist.name
                selectedArtistDegreeField.text = graph.degree(selectedArtist).toString()
                relatedArtistsField.text = graph.getEdges(selectedArtist).map { e -> e.opposite(selectedArtist) }.joinToString("\n") { (it as Artist).name }
            }
        }
    }
}