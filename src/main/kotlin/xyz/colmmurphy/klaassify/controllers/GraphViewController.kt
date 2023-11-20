package xyz.colmmurphy.klaassify.controllers

import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import javafx.scene.text.Text
import xyz.colmmurphy.klaassify.Client
import xyz.colmmurphy.klaassify.collections.Artist
import java.lang.System.gc
import kotlin.math.*
import kotlin.random.Random


class GraphViewController {
    @FXML
    lateinit var title: Text
    @FXML
    lateinit var subtitle: Text
    @FXML
    lateinit var canvas: Canvas

    private val graph = Client.artistGraph

    lateinit var positions: HashMap<Artist, DoubleArray>

    init {

    }
    fun initialize() {
        val random = Random(System.currentTimeMillis())

        positions = eadesSpringEmbedder()

        for (v in graph.vertices) {
            println("${v.name} - ${graph.degree(v)}")
        }

        // perform scaling so that the whole graph fits inside the frame
        // find the largest coordinate and scale all coordinates equally such that this largest coord is now 800
        var largestCoordinate = 0.0
        for (p in positions.values) {
            if (p[0] > largestCoordinate) largestCoordinate = p[0]
            if (p[1] > largestCoordinate) largestCoordinate = p[1]
        }
        for (key in positions.keys) {
            positions[key]!![0] *= 800.0 / largestCoordinate
            positions[key]!![1] *= 800.0 / largestCoordinate
        }

        // draw graph
        val gc = canvas.graphicsContext2D
        gc.clearRect(0.0, 0.0, canvas.width, canvas.height)
        // draw edges first
        gc.stroke = Color.BLACK
        for (i in graph.vertices) {
            for (e in graph.getEdges(i)) {
                val j = e.opposite(i)
                val posI = positions[i]!!
                val posJ = positions[j]!!
                gc.strokeLine(posI[0], posI[1], posJ[0], posJ[1])
            }
        }
        // Draw vertices
        gc.fill = Color.CADETBLUE
        for (i in graph.vertices) {
            val pos: DoubleArray = positions[i]!!
            gc.fillOval(pos[0] - 10.0, pos[1] - 10.0, 20.0, 20.0)
            gc.fillText(i.name, pos[0] - 30, pos[1] + 30)
        }
        gc.stroke()
    }

    /**
     * Function to Render a graph
     * @return hashmap containing vertices as keys, and their coordinates as double arrays
     */
    private fun eadesSpringEmbedder(): HashMap<Artist, DoubleArray> {
        // constants
        val coolingFactor = 0.975
        val l = 40.0
        val cRep = 8.0
        val cAttr = 4.0
        val k = 10_000
        val epsilon = 1.0

        val random = Random(System.currentTimeMillis())
        val p = hashMapOf(*graph.vertices.map {
            it to (doubleArrayOf(random.nextDouble() * canvas.width, random.nextDouble() * canvas.height))
        }.toTypedArray())
        val forces = hashMapOf(*graph.vertices.map { it to doubleArrayOf(0.0, 0.0) }.toTypedArray())

        fun frep(u: Artist, v: Artist): DoubleArray {
            if (u == v) return doubleArrayOf(0.0, 0.0)
            val pu = p[u]!!
            val pv = p[v]!!
            val dx = pu[0] - pv[0]
            val dy = pu[1] - pv[1]
            return doubleArrayOf(cRep / (dx * dx), cRep / (dy * dy))
        }
        fun centerForce(u: Artist): DoubleArray {
            val dx = abs(p[u]!![0] - canvas.width / 2)
            val dy = abs(p[u]!![1] - canvas.width / 2)
            return doubleArrayOf(2000 / (dx * dx), 2000 / (dy * dy))
        }
        fun fattr(u: Artist, v: Artist): DoubleArray {
            if (u == v) return doubleArrayOf(0.0, 0.0)
            val pu = p[u]!!
            val pv = p[v]!!
            val dx = abs(pu[0] - pv[0])
            val dy = abs(pu[1] - pv[1])
            val fx = cAttr * ln(dx / l)
            val fy = cAttr * ln(dy / l)
            val fr = frep(u, v)
            return doubleArrayOf(fx - fr[0], fy - fr[1])
        }

        var prevMaxForce = 0.0
        var delta = 1.0
        var t = 0
        while (t < k) {
            // update forces
            for (u in graph.vertices) {
                val sumForcesRep = doubleArrayOf(0.0, 0.0)
                for (v in graph.vertices) {
                    val f = frep(u, v)
                    sumForcesRep[0] += f[0]
                    sumForcesRep[1] += f[1]
                }
                val sumForcesAttr = doubleArrayOf(0.0, 0.0)
                for (e in graph.getEdges(u)) {
                    val v = e.opposite(u)!! as Artist
                    // no attractive force between non-adjacent vertices
                    if (!graph.areAdjacent(u, v)) continue
                    val f = fattr(u, v)
                    sumForcesAttr[0] += f[0]
                    sumForcesAttr[1] += f[1]
                }
                val fCenter = centerForce(u)
                forces[u] = doubleArrayOf(
                    sumForcesRep[0] + sumForcesAttr[0] + fCenter[0],
                    sumForcesRep[1] + sumForcesAttr[1] + fCenter[1]
                )
            }
            // update positions
            for (u in graph.vertices) {
                p[u]!![0] = p[u]!![0] + (delta * forces[u]!![0])
                p[u]!![1] = p[u]!![1] + (delta * forces[u]!![1])
            }
            // update cooling factor
            delta *= coolingFactor
            t++
            // if max recorded force < epsilon, finish simulating
            var maxForce = 0.0
            forces.values.forEach {
                if (it[0] > maxForce) maxForce = it[0]
                if (it[1] > maxForce) maxForce = it[1]
            }
            println(maxForce)
            if (maxForce < epsilon) {
                return p
            }
            if (maxForce == prevMaxForce) {
                return p
            }
            prevMaxForce = maxForce
        }
        return p
    }
}