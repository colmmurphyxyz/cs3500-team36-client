package xyz.colmmurphy.klaassify.controllers

import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import javafx.scene.text.Text
import xyz.colmmurphy.klaassify.Client
import xyz.colmmurphy.klaassify.collections.Artist
import java.lang.Math.pow
import java.lang.System.gc
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

    private val graph = Client.artistGraph

    object eadesSpringEmbedder {
        private val graph = Client.artistGraph
        private const val idealLength = 75.0
        private const val cRep = 2.0
        private const val cAttr = 1.0
        private const val epsilon = 10.0
        private const val coolingFactor = 0.9875
        private var delta = 1.0
        var iterationsDone = 0
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
                    it to doubleArrayOf(random.nextDouble() * 3500, random.nextDouble() * 3500)
                }.toTypedArray()
            )
            forces = hashMapOf(
                *graph.vertices.map {
                    it to doubleArrayOf(0.0, 0.0)
                }.toTypedArray()
            )
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
//            val dx = abs(pu[0] - pv[0])
//            val dy = abs(pu[1] - pv[1])
            // displacement vector = [ dx dy ]
            val distance = sqrt( (dx * dx) + (dy * dy) )
            val force = cRep / (distance * distance)
            return doubleArrayOf(force * (dx / distance), force * (dy / distance))
        }


        fun doOneIteration() {
            println("on iteration $iterationsDone")
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
            // find max force, print it
            var maxForce = 0.0
            for (i in forces.values) {
                if (abs(i[0]) > abs(maxForce)) maxForce = i[0]
                if (abs(i[1]) > abs(maxForce)) maxForce = i[1]
            }
            println("strongest force: $maxForce")
        }
    }

    private fun drawGraph(p: MutableMap<Artist, DoubleArray>) {
        // scale coords
        var maxValue = 1.0
        for (i in p.values) {
            if (i[0] > maxValue) maxValue = i[0]
            if (i[1] > maxValue) maxValue = i[1]
        }
        val scalingFactor = (canvas.width) / maxValue

        val scale: (Double) -> Double = {i -> (i * scalingFactor)}

        // draw edges
        val gc = canvas.graphicsContext2D
        gc.clearRect(0.0, 0.0, canvas.width, canvas.height)
        gc.stroke()
        // draw edges
        gc.stroke = Color.BLACK
        for (e in graph.edges) {
            val pu = p[e.v1]!!
            val pv = p[e.v2]!!
            gc.strokeLine(scale(pu[0]), scale(pu[1]), scale(pv[0]), scale(pv[1]))
        }
        // draw vertices
        gc.fill = Color.BLUEVIOLET
        for (i in graph.vertices) {
            val pos: DoubleArray = p[i]!!
            gc.fillOval(scale(pos[0]) - 10.0, scale(pos[1]) - 10.0, 20.0, 20.0)
//            gc.fillText(i.name, pos[0] - 30, pos[1] + 30)
        }
        gc.stroke()
    }

    private fun renderGraph() {
        eadesSpringEmbedder.reset()
        val k = 5_000
        val frt = fixedRateTimer("graph render", period = 10L) {
            if (eadesSpringEmbedder.iterationsDone < k) {
                eadesSpringEmbedder.doOneIteration()
                drawGraph(eadesSpringEmbedder.positions)
            } else {
                this.cancel()
            }
        }
    }

    fun initialize() {
        renderGraph()
//        val positions = eadesSpringEmbedder_old()
//        // scaling
//        var maxValue = 1.0
//        for (i in positions.values) {
//            if (i[0] > maxValue) maxValue = i[0]
//            if (i[1] > maxValue) maxValue = i[1]
//        }
//        val scalingFactor = canvas.width / maxValue
//        for (i in positions.values) {
//            i[0] *= scalingFactor
//            i[1] *= scalingFactor
//        }
//        // draw graph
//        val gc = canvas.graphicsContext2D
//        gc.clearRect(0.0, 0.0, canvas.width, canvas.height)
//        // draw edges first
//        gc.stroke = Color.BLACK
//        for (i in graph.vertices) {
//            for (e in graph.getEdges(i)) {
//                val j = e.opposite(i)
//                val posI = positions[i]!!
//                val posJ = positions[j]!!
//                gc.strokeLine(posI[0], posI[1], posJ[0], posJ[1])
//            }
//        }
//        // Draw vertices
//        gc.fill = Color.CADETBLUE
//        for (i in graph.vertices) {
//            val pos: DoubleArray = positions[i]!!
//            gc.fillOval(pos[0] - 10.0, pos[1] - 10.0, 20.0, 20.0)
//            gc.fillText(i.name, pos[0] - 30, pos[1] + 30)
//        }
//        gc.stroke()
    }

    /**
     * Function to Render a graph
     * @return hashmap containing vertices as keys, and their coordinates as double arrays
     */
    private fun eadesSpringEmbedder_old(): HashMap<Artist, DoubleArray> {
        // constants
        val coolingFactor = 0.975
        val l = 40.0
        val cRep = 8.0
        val cAttr = 8.0
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