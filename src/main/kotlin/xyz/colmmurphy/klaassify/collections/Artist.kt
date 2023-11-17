package xyz.colmmurphy.klaassify.collections

import javafx.scene.image.Image

/**
 * Data class to represent an artist
 * Extends the Vertex class for use in a graph
 */
data class Artist(
    val name: String,
    val genres: Set<String>,
    val followers: Long,
    val spotifyUrl: String,
    val popularity: Int,
    val images: List<Image>
) : Vertex() {
    override val element: Artist
        get() = this
}
