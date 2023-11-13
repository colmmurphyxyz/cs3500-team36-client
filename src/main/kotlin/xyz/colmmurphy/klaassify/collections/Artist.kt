package xyz.colmmurphy.klaassify.collections

/**
 * Data class to represent an artist
 * Implements the IVertex interface for use in a graph
 */
data class Artist(
    val name: String,
    val genres: Set<String>,
    val followers: Long,
    val spotifyUrl: String,
    val popularity: Int,
    val images: List<ArtistImage>
) : IVertex {
    override val element: Artist
        get() = this
}
