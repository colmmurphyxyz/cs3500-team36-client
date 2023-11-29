package xyz.colmmurphy.klaassify.collections

import javafx.scene.image.Image
import kotlinx.serialization.Serializable

/**
 * Data class to represent an artist
 * Extends the Vertex class for use in a graph
 */
@Serializable
data class Artist(
    val name: String,
    val genres: Set<String>,
    val followers: Long,
    val url: String,
    val popularity: Int,
) : Vertex() {
    override val element: Artist
        get() = this
}
