package xyz.colmmurphy.klaassify.controllers

import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.text.Text
import xyz.colmmurphy.klaassify.Client
import kotlin.random.nextInt

class GraphViewController {
    @FXML
    lateinit var title: Text
    @FXML
    lateinit var subtitle: Text
    @FXML
    lateinit var canvas: Canvas

    fun initialize() {
        canvas.style = "-fx-border: 5px solid black;"
        val gc = canvas.graphicsContext2D
        gc.stroke = Color.FORESTGREEN
        val r = kotlin.random.Random(2)
        for (artist in Client.artistGraph.vertices) {
            val x = r.nextInt(800)
            val y = r.nextInt(800)
            gc.strokeOval(x.toDouble(), y.toDouble(), 30.0, 30.0)
        }
        gc.stroke()
    }
}